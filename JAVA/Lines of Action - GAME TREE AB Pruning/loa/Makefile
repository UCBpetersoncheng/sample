# A simple-minded makefile that just calls the one in the outer source
# directory.

.PHONY: default clean check style

default: 
	$(MAKE) -C .. compile

clean:
	$(MAKE) -C .. clean

check:
	$(MAKE) -C .. check

style:
	$(MAKE) -C .. style
