## Containers

significantly smaller overhead than a full virtual machine, as they share the kernel of the host operating system and can therefore start extremely fast.
containers usually contain a single process and its dependencies; and a container's start-up time can be determined by the amount of time the containerised process takes to start.
drawback - share same operating kernel, and so all containers must use same operating system.
i.e. docker containers must run Linux distributions and cannot run windows/Linux processes.

Docker - tool for easy programmatic creation and distribution of container images as well as launching and deploying containers themselves. simple command line tools and HTTP APIs that make managing containers easy to automate and creates a homogeneous system for running applications.

