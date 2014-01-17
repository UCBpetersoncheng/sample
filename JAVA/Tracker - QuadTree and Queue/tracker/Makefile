# A simple-minded makefile that just calls the one in the outer source
# directory.

.PHONY: default clean check

default: 
	$(MAKE) -C .. default

clean:
	$(MAKE) -C .. clean

check:
	$(MAKE) -C .. check-tracker

style:
	$(MAKE) -C .. style
