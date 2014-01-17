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

.PHONY: default style check clean

SHELL = bash

SRC = $(wildcard enigma/*.java)

default: enigma/Main.class

enigma/Main.class: $(SRC)
	javac -g $(SRC)

style:
	style61b enigma/*.java

check: enigma/Main.class
	@code=0; \
	echo "Testing correct inputs:"; \
	if ! bash test-correct tests/*.inp; then code=1; fi; \
	echo; echo "Testing erroneous inputs:"; \
	if ! bash test-error tests/*.err; then code=1; fi; \
	test $$code -eq 0

clean:
	$(RM) enigma/*.class */*~ *~ OUT


