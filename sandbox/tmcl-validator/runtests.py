
import validator
from glob import glob
from net.ontopia.topicmaps.utils import ImportExportUtils

BASE = "/Users/larsga/cvs-co/cxtm-tests/trunk/tmcl/level-1/"
INVALID = "invalid/"
VALID = "valid/"
PATTERN = "*.ctm"

for file in glob(BASE + INVALID + PATTERN):
    tm = ImportExportUtils.getReader(file).read()
    errors = validator.validate(tm)

    if not errors:
        print "No errors in", file
    elif len(errors) != 1:
        print "More than one error in", file
        validator.report(errors)

for file in glob(BASE + VALID + PATTERN):
    tm = ImportExportUtils.getReader(file).read()
    errors = validator.validate(tm)

    if errors:
        print "Errors in", file
        validator.report(errors)
        