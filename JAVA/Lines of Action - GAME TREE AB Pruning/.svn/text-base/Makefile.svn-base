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

# Flags to pass to Java compilations (include debugging info and report
# "unsafe" operations.)
JFLAGS = -g -Xlint:unchecked -Xlint:deprecated

SRCS = $(wildcard loa/*.java)

CLASSES = $(SRCS:.java=.class)

# Tests
ALL_TESTS = $(wildcard tests/*.inp)

# Tell make that these are not really files.
.PHONY: clean default compile style  \
	check unit blackbox

# By default, make sure all classes are present and check if any sources have
# changed since the last build.
default: compile

compile: $(CLASSES)

style:
	style61b $(SRCS) 

$(CLASSES): loa/sentinel

loa/sentinel: $(SRCS)
	javac $(JFLAGS) $(SRCS)
	touch $@

# Run Tests.
check: unit blackbox

# Run util Junit tests.
unit: $(CLASSES)
	java loa.UnitTest

# Run all blackbox tests using my tracker and util packages.
# The test-loa script will be in the ~cs61b/bin directory.
blackbox: default
	python test-loa $(ALL_TESTS)

# Find and remove all *~, *.class, and *.out files, and the generated jar
# files.  Do not touch .svn directories.
clean :
	$(RM) */sentinel
	find . -name .svn -prune -o \
            \( -name '*.out' -o -name '*.class' -o -name '*~' \) \
            -exec $(RM) {} \;
