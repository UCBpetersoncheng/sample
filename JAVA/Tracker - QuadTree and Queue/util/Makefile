# A simple-minded makefile that just calls the one in the outer source
# directory.

.PHONY: default clean check

default: 
	$(MAKE) -C .. compile-util

clean:
	$(MAKE) -C .. clean

check:
	$(MAKE) -C .. check-util

style:
	$(MAKE) -C .. style