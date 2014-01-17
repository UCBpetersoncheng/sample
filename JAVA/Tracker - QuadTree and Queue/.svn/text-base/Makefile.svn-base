# This a Makefile, an input file for the 'make' program.  For you 
# command-line and Emacs enthusiasts, this makes it possible to build
# this program with a single command:
#     gmake 
# (or just 'make' if you are on a system that uses GNU make by default,
# such as Linux.) You can also clean up junk files and .class files with
#     gmake clean
# To run style61b (our style enforcer) over your source files, type
#     gmake style
# Finally, you can run any tests you'd care to with
#     gmake check
# This first runs your program on the input files tests/*.inp and checks
# to see that it completes normally and that the results are those in the
# files tests/*.out.  It also runs the program on the input files tests/*.err
# and checks that the program properly reports an error as given in the
# specification. It's up to you to come up with the test files; the 
# skeleton just includes some simple samples.

# On the instructional machines, we've defined the environment variable
# $STAFFLIBDIR to be ~cs61b/lib, where we keep .jar (Java Archive) files
# containing staff classes.  At home, you can define STAFFLIBDIR to point at
# wherever you keep copies of these files.

# Flags to pass to Java compilations (include debugging info and report
# "unsafe" operations.)
JFLAGS = -g -Xlint:unchecked

STAFF_TRACKER = $(STAFFLIBDIR)/proj2-tracker.jar
STAFF_UTIL = $(STAFFLIBDIR)/proj2-util.jar

OTHER_JARS = $(STAFFLIBDIR)/ucb.jar:$(STAFFLIBDIR)/ucb-f2007.jar:$(STAFFLIBDIR)/junit.jar

# Sources
UTIL_SRCS := $(wildcard util/*.java)
TRACKER_SRCS := $(wildcard tracker/*.java)

SRCS = $(UTIL_SRCS) $(TRACKER_SRCS)

# Classes
UTIL_CLASSES = $(UTIL_SRCS:.java=.class)
TRACKER_CLASSES = $(TRACKER_SRCS:.java=.class)

# Tests
ALL_TESTS = $(wildcard tests/*.trk)

# Tell make that these are not really files.
.PHONY: clean default compile-util compile-tracker style \
	check check-util check-tracker unit-util unit-tracker  \
	blackbox-util blackbox-tracker blackbox


# By default, make sure all classes are present and check if any sources have
# changed since the last build.
default: compile-util compile-tracker

compile-util: $(UTIL_CLASSES)

compile-tracker: $(TRACKER_CLASSES)

style:
	style61b $(UTIL_SRCS) $(TRACKER_SRCS)

$(UTIL_CLASSES): util/sentinel

$(TRACKER_CLASSES): tracker/sentinel

util/sentinel: $(UTIL_SRCS)
	javac $(JFLAGS) $(UTIL_SRCS)
	touch $@

tracker/sentinel: $(TRACKER_SRCS)
	javac $(JFLAGS) $(TRACKER_SRCS)
	touch $@

# Create a JAR file out of my tracker classes
tracker.jar: $(TRACKER_CLASSES)
	jar cf $@ `find tracker -name '*.class'`

# Create a JAR file out of my util classes
util.jar: $(UTIL_CLASSES)
	jar cf $@ `find util -name '*.class'`

# Run Tests.
check: check-util check-tracker

check-util: unit-util

check-tracker: unit-tracker blackbox

# Run util Junit tests.
unit-util: $(UTIL_CLASSES)
	java util.UnitTest

# Run tracker Junit tests.
unit-tracker: default
	java tracker.UnitTest

# Run all blackbox tests using my tracker and util packages.
blackbox: default
	@echo 
	@echo "Running tests of both packages..."
	python test-tracker $(ALL_TESTS)

# Run all blackbox tests using my util package and the staff tracker package. 
blackbox-util: util.jar
	@echo 
	@echo "Running tests of util package against staff tracker package..."
	CLASSPATH=util.jar:$(STAFF_TRACKER):$(OTHER_JARS) \
            python test-tracker $(ALL_TESTS)

# Run all blackbox tests using my tracker package and the staff util package. 
blackbox-tracker: tracker.jar
	@echo 
	@echo "Running tests of tracker package against staff util package..."
	CLASSPATH=tracker.jar:$(STAFF_UTIL):$(OTHER_JARS) \
	    python test-tracker $(ALL_TESTS)

# Find and remove all *~, *.class, and *.out files, and the generated jar
# files.  Do not touch .svn directories.
clean :
	$(RM) */sentinel
	find . -name .svn -prune -o \
            \( -name '*.out' -o -name '*.class' -o -name '*~' \) \
            -exec $(RM) {} \;
	$(RM) tracker.jar util.jar
