VERSION?=beta
REGISTRY?=g4s8
NAME?=qabot
DCONFIG?=

release: clean build push clean

build:
	docker build --build-arg="version=$(VERSION)" -t $(REGISTRY)/$(NAME):$(VERSION) .

clean:
	docker rm -f $(REGISTRY)/$(NAME):$(VERSION) 2> /dev/null || true

push:
	docker --config=$(DCONFIG) push $(REGISTRY)/$(NAME):$(VERSION)

.PHONY: release clean build push
