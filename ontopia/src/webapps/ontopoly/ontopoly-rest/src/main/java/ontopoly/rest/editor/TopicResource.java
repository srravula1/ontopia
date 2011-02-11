package ontopoly.rest.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import ontopoly.rest.editor.Utils.Link;
import ontopoly.rest.editor.spi.PrestoDataProvider;
import ontopoly.rest.editor.spi.PrestoFieldUsage;
import ontopoly.rest.editor.spi.PrestoSchemaProvider;
import ontopoly.rest.editor.spi.PrestoSession;
import ontopoly.rest.editor.spi.PrestoTopic;
import ontopoly.rest.editor.spi.PrestoType;
import ontopoly.rest.editor.spi.PrestoView;
import ontopoly.rest.editor.spi.impl.couchdb.CouchDataProvider;
import ontopoly.rest.editor.spi.impl.pojo.PojoSchemaProvider;
import ontopoly.rest.editor.spi.impl.pojo.PojoSession;

import org.codehaus.jettison.json.JSONObject;

@Path("/editor")
public class TopicResource {

  // TODO: add more endpoints: 
  //
  // 1: / - information about server and link to /available-topicmaps
  // 2: /available-topicmaps - lists available topic maps
  // 3: /create-instance/{topicMapId}

  private TopicListener topicListener;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("")
  public Map<String,Object> getRootInfo(@Context UriInfo uriInfo) throws Exception {

    Map<String,Object> result = new LinkedHashMap<String,Object>();

    result.put("id", uriInfo.getBaseUri() + "editor");
    result.put("version", 0);
    result.put("name", "Ontopoly Editor REST API");

    List<Link> links = new ArrayList<Link>();
    links.add(new Link("available-topicmaps", uriInfo.getBaseUri() + "editor/available-topicmaps"));
    result.put("links", links);      
    return result;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("available-topicmaps")
  public Map<String,Object> getTopicMaps(@Context UriInfo uriInfo) throws Exception {

    Map<String,Object> result = new LinkedHashMap<String,Object>();

    result.put("id", "topicmaps");
    result.put("name", "Ontopoly Editor REST API");

    List<Map<String,Object>> topicmaps = new ArrayList<Map<String,Object>>();
    
    Map<String,Object> topicmap = new LinkedHashMap<String,Object>();
    topicmap.put("id", "litteraturklubben.xtm");
    topicmap.put("name", "Litteraturklubben");

    List<Link> links = new ArrayList<Link>();
    links.add(new Link("edit", uriInfo.getBaseUri() + "editor/topicmap-info/litteraturklubben.xtm"));
    topicmap.put("links", links);    
    
    topicmaps.add(topicmap);
    result.put("topicmaps", topicmaps);      
    return result;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("topicmap-info/{topicMapId}")
  public Map<String,Object> getTopicMapInfo(
      @Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId) throws Exception {

    PrestoSession session = createSession(topicMapId);
    try {
      Map<String,Object> result = new LinkedHashMap<String,Object>();

      result.put("id", session.getDatabaseId());
      result.put("name", session.getDatabaseName());

      List<Link> links = new ArrayList<Link>();
      links.add(new Link("available-types-tree", uriInfo.getBaseUri() + "editor/available-types-tree/" + session.getDatabaseId()));
      links.add(new Link("available-types-tree-lazy", uriInfo.getBaseUri() + "editor/available-types-tree-lazy/" + session.getDatabaseId()));
      links.add(new Link("edit-topic-by-id", uriInfo.getBaseUri() + "editor/topic/" + session.getDatabaseId() + "/{topicId}"));
      result.put("links", links);      
      return result;


    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("create-instance/{topicMapId}/{topicTypeId}")
  public Map<String,Object> createInstance(
      @Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId, 
      @PathParam("topicTypeId") final String topicTypeId) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();

    try {

      PrestoType topicType = schemaProvider.getTypeById(topicTypeId);
      PrestoView fieldsView = topicType.getDefaultView();

      return Utils.getNewTopicInfo(uriInfo, topicType, fieldsView);

    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("create-field-instance/{topicMapId}/{parentTopicId}/{parentFieldId}/{playerTypeId}")
  public Map<String,Object> createInstance(
      @Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId,
      @PathParam("parentTopicId") final String parentTopicId,
      @PathParam("parentFieldId") final String parentFieldId, 
      @PathParam("playerTypeId") final String playerTypeId) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();
    
    try {

      PrestoType topicType = schemaProvider.getTypeById(playerTypeId);
      PrestoView fieldsView = topicType.getDefaultView();

      return Utils.getNewTopicInfo(uriInfo, topicType, fieldsView, parentTopicId, parentFieldId);

    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("topic-data/{topicMapId}/{topicId}")
  public Map<String,Object> getTopicData(
      @Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId, 
      @PathParam("topicId") final String topicId) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();
    PrestoDataProvider dataProvider = session.getDataProvider();
    
    try {

      PrestoTopic topic = dataProvider.getTopicById(topicId);
      PrestoType topicType = schemaProvider.getTypeById(topic.getTypeId());
      
      return Utils.getTopicData(uriInfo, topic, topicType);

    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("topic/{topicMapId}/{topicId}")
  public Map<String,Object> getTopicInDefaultView(
      @Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId, 
      @PathParam("topicId") final String topicId) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();
    PrestoDataProvider dataProvider = session.getDataProvider();
    
    try {

      PrestoTopic topic = dataProvider.getTopicById(topicId);
      PrestoType topicType = schemaProvider.getTypeById(topic.getTypeId());
      PrestoView fieldsView = topicType.getDefaultView();
      
      return Utils.getTopicInfo(uriInfo, topic, topicType, fieldsView);

    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("topic/{topicMapId}/{topicId}/{viewId}")
  public Map<String,Object> getTopicInView(@Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId, 
      @PathParam("topicId") final String topicId,
      @PathParam("viewId") final String viewId) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();
    PrestoDataProvider dataProvider = session.getDataProvider();

    try {

      PrestoTopic topic = dataProvider.getTopicById(topicId);
      PrestoType topicType = schemaProvider.getTypeById(topic.getTypeId());
      PrestoView fieldsView = topicType.getViewById(viewId);

      return Utils.getTopicInfo(uriInfo, topic, topicType, fieldsView);

    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    }
  }

//  @GET
//  @Produces(MediaType.APPLICATION_JSON)
//  @Path("topic-info/{topicMapId}/{topicId}")
//  public Map<String,Object> getTopicInfo(@Context UriInfo uriInfo, 
//      @PathParam("topicMapId") final String topicMapId, 
//      @PathParam("topicId") final String topicId) throws Exception {
//
//    TopicMapStoreIF store = TopicMaps.createStore(topicMapId, true);
//
//    try {
//      TopicMap topicMap = new TopicMap(store.getTopicMap(), topicMapId);
//      Topic topic = topicMap.getTopicById(topicId);
//
//      TopicType topicType = OntopolyUtils.getDefaultTopicType(topic);
//
//      FieldsView fieldsView = FieldsView.getDefaultFieldsView(topicMap);
//
//      Map<String,Object> result = new LinkedHashMap<String,Object>();
//
//      result.put("id", topic.getId());
//      result.put("views", Utils.getViews(uriInfo, topic, topicType, fieldsView));
//      return result;
//
//    } catch (Exception e) {
//      store.abort();
//      throw e;
//    } finally {
//      store.close();      
//    }
//  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("topic/{topicMapId}/{topicId}/{viewId}")
  public Map<String,Object> updateTopic(@Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId, 
      @PathParam("topicId") final String topicId, 
      @PathParam("viewId") final String viewId, JSONObject jsonObject) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();
    PrestoDataProvider dataProvider = session.getDataProvider();

    try {

      PrestoTopic topic = null;
      PrestoType topicType;
      if (topicId.startsWith("_")) {
        topicType = schemaProvider.getTypeById(topicId.substring(1));
      } else {
        topic = dataProvider.getTopicById(topicId);
        topicType = schemaProvider.getTypeById(topic.getTypeId());
      }

      PrestoView fieldsView = topicType.getViewById(viewId);

      Map<String, Object> result = Utils.updateTopic(uriInfo, session, topic, topicType, fieldsView, jsonObject);
      String id = (String)result.get("id");
      session.commit();
      onTopicUpdated(id);

      return result;
    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();
    }
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("add-field-values/{topicMapId}/{topicId}/{viewId}/{fieldId}")
  public Map<String,Object> addFieldValues(@Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId, 
      @PathParam("topicId") final String topicId, 
      @PathParam("viewId") final String viewId,
      @PathParam("fieldId") final String fieldId, JSONObject jsonObject) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();
    PrestoDataProvider dataProvider = session.getDataProvider();

    try {

      PrestoTopic topic = dataProvider.getTopicById(topicId);
      PrestoType topicType = schemaProvider.getTypeById(topic.getTypeId());
      PrestoView fieldsView = topicType.getViewById(viewId);

      PrestoFieldUsage field = topicType.getFieldById(fieldId, fieldsView);

      Map<String, Object> result = Utils.addFieldValues(uriInfo, session, topic, field, jsonObject);

      String id = topic.getId();

      session.commit();
      onTopicUpdated(id);

      return result;
    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    } 
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("remove-field-values/{topicMapId}/{topicId}/{viewId}/{fieldId}")
  public Map<String,Object> removeFieldValues(@Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId, 
      @PathParam("topicId") final String topicId, 
      @PathParam("viewId") final String viewId,
      @PathParam("fieldId") final String fieldId, JSONObject jsonObject) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();
    PrestoDataProvider dataProvider = session.getDataProvider();

    try {

      PrestoTopic topic = dataProvider.getTopicById(topicId);
      PrestoType topicType = schemaProvider.getTypeById(topic.getTypeId());
      PrestoView fieldsView = topicType.getViewById(viewId);

      PrestoFieldUsage field = topicType.getFieldById(fieldId, fieldsView);

      Map<String, Object> result =  Utils.removeFieldValues(uriInfo, session, topic, field, jsonObject);

      String id = topic.getId();

      session.commit();
      onTopicUpdated(id);

      return result;
    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    } 
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("available-field-values/{topicMapId}/{topicId}/{viewId}/{fieldId}")
  public Map<String,Object> getAvailableFieldValues(@Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId, 
      @PathParam("topicId") final String topicId, 
      @PathParam("viewId") final String viewId,
      @PathParam("fieldId") final String fieldId) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();
    PrestoDataProvider dataProvider = session.getDataProvider();

    try {

      PrestoTopic topic;
      PrestoType topicType;
      if (topicId.startsWith("_")) {
        topicType = schemaProvider.getTypeById(topicId.substring(1));
        topic  = null;
      } else {
        topic = dataProvider.getTopicById(topicId);
        topicType = schemaProvider.getTypeById(topic.getTypeId());
      }

      PrestoView fieldsView = topicType.getViewById(viewId);
      
      PrestoFieldUsage field = topicType.getFieldById(fieldId, fieldsView);
      
      Collection<PrestoTopic> availableFieldValues = dataProvider.getAvailableFieldValues(field);
      return createFieldInfoAllowed(uriInfo, field, availableFieldValues);

    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    }
  }

  private Map<String,Object> createFieldInfoAllowed(UriInfo uriInfo, PrestoFieldUsage field, Collection<PrestoTopic> availableFieldValues) {

    Map<String,Object> result = new LinkedHashMap<String,Object>();
    result.put("id", field.getId());
    result.put("name", field.getName());

    List<Object> values = new ArrayList<Object>(availableFieldValues.size());
    if (!availableFieldValues.isEmpty()) {
      
      PrestoView valueView = field.getValueView();
      boolean traversable = field.isTraversable();
      
      for (PrestoTopic value : availableFieldValues) {
        values.add(Utils.getAllowedTopicFieldValue(uriInfo, value, valueView, traversable));
      }
    } 
    result.put("values", values);

    return result;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("available-field-types/{topicMapId}/{topicId}/{viewId}/{fieldId}")
  public Map<String,Object> getAvailableFieldTypes(@Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId, 
      @PathParam("topicId") final String topicId, 
      @PathParam("viewId") final String viewId,
      @PathParam("fieldId") final String fieldId) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();
    PrestoDataProvider dataProvider = session.getDataProvider();

    try {
      
      PrestoTopic topic = dataProvider.getTopicById(topicId);
      PrestoType topicType = schemaProvider.getTypeById(topic.getTypeId());
      PrestoView fieldsView = topicType.getViewById(viewId);

      PrestoFieldUsage field = topicType.getFieldById(fieldId, fieldsView);
      
      Map<String,Object> result = new LinkedHashMap<String,Object>();
      result.put("id", field.getId());
      result.put("name", field.getName());
      
      Collection<PrestoType> availableFieldCreateTypes = field.getAvailableFieldCreateTypes();

      List<Object> types = new ArrayList<Object>(availableFieldCreateTypes.size());
      for (PrestoType playerType : availableFieldCreateTypes) {
        types.add(Utils.getCreateFieldInstance(uriInfo, topic, field, playerType));
      }

      result.put("types", types);
      return result;
      
    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("available-types-tree-lazy/{topicMapId}")
  public Map<String,Object> getAvailableTypesTreeLazy(@Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();

    try {

      Map<String,Object> result = new LinkedHashMap<String,Object>();
      result.put("types", TypeUtils.getAvailableTypesTreeLazy(uriInfo, schemaProvider.getRootTypes()));      
      return result;

    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("available-types-tree-lazy/{topicMapId}/{typeId}")
  public Map<String,Object> getAvailableTypesTreeLazy(@Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId, 
      @PathParam("typeId") final String typeId) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();

    try {
      PrestoType type = schemaProvider.getTypeById(typeId);

      Map<String,Object> result = new LinkedHashMap<String,Object>();
      result.put("types", TypeUtils.getAvailableTypesTreeLazy(uriInfo, type.getDirectSubTypes()));      
      return result;

    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("available-types-tree/{topicMapId}")
  public Map<String,Object> getAvailableTypesTree(@Context UriInfo uriInfo, 
      @PathParam("topicMapId") final String topicMapId) throws Exception {

    PrestoSession session = createSession(topicMapId);
    PrestoSchemaProvider schemaProvider = session.getSchemaProvider();

    try {

      Map<String,Object> result = new LinkedHashMap<String,Object>();
      result.put("types", TypeUtils.getAvailableTypesTree(uriInfo, schemaProvider.getRootTypes()));      
      return result;

    } catch (Exception e) {
      session.abort();
      throw e;
    } finally {
      session.close();      
    }
  }

  protected void onTopicUpdated(String topicId) {
    topicListener.onTopicUpdated(topicId);
  }
  
  protected PrestoSession createSession(String topicMapId) {
//    // schema and data stored in ontopia
//    OntopolySession session = new OntopolySession(topicMapId);
//    session.setStableIdPrefix("sek:");
//    return session;

    // schema stored in ontopia and data stored in couchdb
    CouchDataProvider dataProvider = new CouchDataProvider("localhost", 5984, "presto", "_design/presto");

    PojoSchemaProvider schemaProvider = PojoSchemaProvider.getSchemaProvider(topicMapId, topicMapId + ".presto.json");
    PojoSession session = new PojoSession(topicMapId, topicMapId, schemaProvider, dataProvider);
    
//    OntopolySession session = new OntopolySession(topicMapId, dataProvider);
//    session.setStableIdPrefix("sek:");
    return session;
  }
  
  @Context
  public void setServletContext(ServletContext servletContext) {
    // set up topic listener
    String listenerClassName = servletContext.getInitParameter("ontopoly-rest.listener");
    if (listenerClassName != null) {
      try {
        Class<?> listenerClass = Class.forName(listenerClassName);
        this.topicListener = (TopicListener) listenerClass.newInstance();

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else {
      this.topicListener = new TopicListener() {
        public void onTopicUpdated(String topicId) {
        }
      };
    }
  }

}
