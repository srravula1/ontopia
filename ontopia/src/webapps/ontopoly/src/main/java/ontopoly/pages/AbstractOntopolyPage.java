package ontopoly.pages;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.ontopia.topicmaps.core.TopicIF;
import net.ontopia.topicmaps.core.TopicNameIF;
import ontopoly.OntopolySession;
import ontopoly.model.FieldDefinition;
import ontopoly.model.FieldInstance;
import ontopoly.model.LifeCycleListener;
import ontopoly.model.PSI;
import ontopoly.model.RoleField;
import ontopoly.model.Topic;
import ontopoly.model.TopicType;
import ontopoly.utils.OntopolyModelUtils;
import ontopoly.utils.OntopolyUtils;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractOntopolyPage extends WebPage implements LifeCycleListener {
 
  protected static Logger log = LoggerFactory.getLogger(AbstractOntopolyPage.class);
  
  private boolean isReadOnlyPage;
  
  public AbstractOntopolyPage() {	  
  }
  
  public AbstractOntopolyPage(PageParameters params) {
    super(params);
    
    // add header contributor for stylesheet
    add(CSSPackageResource.getHeaderContribution(getStylesheet()));
  }

  protected String getStylesheet() {
    return "resources/ontopoly.resources.Resources/stylesheet.css";
  }
  
  public OntopolySession getOntopolySession() { 
    return (OntopolySession) getSession(); 
  }  
  
  public boolean isShortcutsEnabled() { 
    return getOntopolySession().isShortcutsEnabled(); 
  }
    
  public boolean isAnnotationEnabled() { 
    return getOntopolySession().isAnnotationEnabled(); // || isAdministrationEnabled(); 
  }

  public boolean isAdministrationEnabled() {
    return getOntopolySession().isAdministrationEnabled();
  }
  
  public boolean isReadOnlyPage() {
    return isReadOnlyPage;
  }

  public void setReadOnlyPage(boolean isReadOnlyPage) {
    this.isReadOnlyPage = isReadOnlyPage;
  }

  public boolean isAddAllowed(Topic parent, FieldDefinition fdParent, Topic child, FieldDefinition fdChild) {
//    System.out.println("AA: " + parent + " " + child);
    return true;
  }
  
  public boolean isAddAllowed(Topic parent, FieldDefinition fdParent) {
//    System.out.println("AAu: " + parent);
    return true;
  }
  
  public boolean isRemoveAllowed(Topic parent, FieldDefinition fdParent, Topic child, FieldDefinition fdChild) {
//    System.out.println("RA: " + parent + " " + child);
    return true;
  }
  
  public boolean isRemoveAllowed(Topic parent, FieldDefinition fdParent) {
//    System.out.println("RAu: " + parent);
    return true;
  }
  
  public boolean isCreateAllowed(Topic parent, FieldDefinition fdParent, TopicType childType, FieldDefinition fdChild) {
//    System.out.println("CA: " + parent + " " + childType);
    return true;
  }

  public LifeCycleListener getListener() {
    return this;
  }
  
  // LifeCycleListener implementation
  
  public void onAfterCreate(Topic topic, TopicType topicType) {    
//    System.out.println("oAC: " + topic + " " + topicType);
  }

  public void onBeforeDelete(Topic topic) {
//    System.out.println("oBD: " + topic);    
  }

  public void onAfterAdd(FieldInstance fieldInstance, Object value) {
//    System.out.println("oAA: " + fieldInstance + " " + value);
    FieldDefinition fieldDefinition = fieldInstance.getFieldAssignment().getFieldDefinition(); 

    // add name scoped by role type to association type
    if (fieldDefinition.getFieldType() == FieldDefinition.FIELD_TYPE_NAME) {
      Topic topic = fieldInstance.getInstance();
      if (topic.isInstanceOf(PSI.ON_ROLE_FIELD)) {
        RoleField rfield = new RoleField(topic.getTopicIF(), topic.getTopicMap());
        TopicIF atype = rfield.getAssociationField().getAssociationType().getTopicIF();
        TopicIF rtype = rfield.getRoleType().getTopicIF();
        if (atype != null && rtype != null) {
          Collection<TopicIF> scope = Collections.singleton(rtype);
          List<TopicNameIF> names = OntopolyModelUtils.findTopicNames(null, atype, (String)value, scope);
          if (names.isEmpty()) {
            OntopolyModelUtils.makeTopicName(null, atype, (String)value, scope);
          } else {
            // remove all except the first one
            Iterator iter = names.iterator();
            iter.next();
            while (iter.hasNext()) {
              TopicNameIF name = (TopicNameIF) iter.next();
              name.remove();
            }
          }
        }
      }
    }
  }

  public void onBeforeRemove(FieldInstance fieldInstance, Object value) {    
//    System.out.println("oBR: " + fieldInstance + " " + value);
    FieldDefinition fieldDefinition = fieldInstance.getFieldAssignment().getFieldDefinition(); 

    // remove name scoped by role type from association type
    if (fieldDefinition.getFieldType() == FieldDefinition.FIELD_TYPE_NAME) {
      Topic topic = fieldInstance.getInstance();
      if (topic.isInstanceOf(PSI.ON_ROLE_FIELD)) {
        RoleField rfield = new RoleField(topic.getTopicIF(), topic.getTopicMap());
        TopicIF atype = rfield.getAssociationField().getAssociationType().getTopicIF();
        TopicIF rtype = rfield.getRoleType().getTopicIF();
        if (atype != null && rtype != null) {
          Collection<TopicIF> scope = Collections.singleton(rtype);
          List<TopicNameIF> names = OntopolyModelUtils.findTopicNames(null, atype, (String)value, scope);
          if (!names.isEmpty()) {
            Iterator iter = names.iterator();
            while (iter.hasNext()) {
              TopicNameIF name = (TopicNameIF) iter.next();
              name.remove();
            }
          }
        }
      }
    }
  }
  
  /**
   * Access filter to tell whether user has default access rights to topic is not.
   * @param topic the topic to check rights for
   * @return true if access allowed
   */
  public boolean filterTopic(Topic topic) {
    if (isAdministrationEnabled())
      return OntopolyUtils.filterTopicByAdministratorRole(topic);
    else if (isAnnotationEnabled())
      return OntopolyUtils.filterTopicByAnnotationRole(topic);
    else
      return OntopolyUtils.filterTopicByDefaultRole(topic);
  }

  /**
   * Checks access for a collection of topics. Topics that the user does 
   * not have access to will be removed from the collection.
   * @param topics the topics to check rights for
   */
  public void filterTopics(Collection<? extends Topic> topics) {
    if (isAdministrationEnabled())
      OntopolyUtils.filterTopicsByAdministratorRole(topics);
    else if (isAnnotationEnabled())
      OntopolyUtils.filterTopicsByAnnotationRole(topics);
    else
      OntopolyUtils.filterTopicsByDefaultRole(topics);
  }
  
  /**
   * Returns the display name of the given topic. This method is meant as 
   * an extension point for retrieval of topic names.
   * @param topic
   * @return
   */
  public String getLabel(Topic topic) {
    if (topic instanceof FieldDefinition)
      return ((FieldDefinition)topic).getFieldName();
    else
      return topic.getName();
  }
 
  /**
   * Given the topic return the page class to use. This method is used 
   * in various places around the application to generate links to topics.
   * Subclasses may override it.
   * @param topic
   * @return
   */
  public Class<? extends Page> getPageClass(Topic topic) {
    return getClass();
  }
  
  /**
   * Given the topic return the page parameters to use. This method is used 
   * in various places around the application to generate links to topics. 
   * Subclasses may override it.
   * @param topic
   * @return
   */
  public PageParameters getPageParameters(Topic topic) {
    // WARNING: if you do a change here then you may also want to do so in EmbeddedInstancePage.
    PageParameters params = new PageParameters();
    params.put("topicMapId", topic.getTopicMap().getId());
    params.put("topicId", topic.getId());
    PageParameters thisParams = getPageParameters();
    if (thisParams != null) {
      // forward ontology parameter (if applicable)
      String ontology = thisParams.getString("ontology");
      if (ontology != null && topic.isOntologyTopic())
        params.put("ontology", "true");
    }    
    return params;    
  }
  
}
