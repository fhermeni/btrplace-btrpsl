Btrplace Specification Language
===============================

The btrplace specification language (btrpsl) allows to express constraints
related to the placement of virtual machines in a datacenters.

This language is dedicated to datacenters administrators and applications administrators
that use [Btrplace](http://btrp.inria.fr) to manage their nodes and virtual machines.

Basic examples
--------------------------------


### Describing a datacenter ###

The following example specifies a datacenter composed of 251 nodes. Nodes
are stacked by 40 into racks. 250 of the nodes are working nodes, dedicated
 to the hosting of client. They are labelled from "node-1" to "node-250".
The last node is a service node and run some service VMs. This node is labelled
"node-frontend". The whole datacenter can not run more than 2000 VMs
simultaneously while each node can not host more than 15 VMs at the same time.

    namespace datacenter;

    $nodes = @node-[1..250,frontend];
    capacity($nodes, 2000);

    for $n in $nodes {
        capacity($n, 15);
    }

    $R[1..7] = $nodes % 40;

    export $nodes,$R[1..7] to *;


### Describing a virtualized application ###

Following example is a specification from a application administrator
that describes a 3-tiers Web applications. Each replica of a same
tier should be placed on a distinct nodes for fault tolerance to hardware
failures, while the last tier must be running into a single rack to have a
low latency. Last, the application administrator does not want its VMs to be
collocated with other VMs for security purpose.

    namespace myApplication;

    import datacenter;

    VM[1..10] : tinyInstance<clone,boot=7,halt=10>;
    VM[11..20] : microInstance;
    VM[21..24] : largeMemoryInstance;

    $T1 = VM[1..10];
    $T2 = VM[11..20];
    $T3 = VM[21..24];

    lonely(VM[1..24]);
    for $t in $T[1..3] {
        spread($t);
    }

    among($T3, $R[1..7]);


A drafty and badly written tutorial about the language is available in the `doc` directory. A complete manual
for the language, including its specifications and other examples will be available ... one day.

## Usage ##

The maven artifact is available through a private repository
so you have first to edit your `pom.xml` to declare it:

```xml
<repositories>
    <repository>
        <id>btrp-releases</id>
        <url>http://btrp.inria.fr:8080/repos/releases</url>
    </repository>
    <repository>
        <id>btrp-snapshots</id>
        <url>http://btrp.inria.fr:8080/repos/snapshot-releases</url>
    </repository>
</repositories>
```

Next, just declare the dependency:

```xml
<dependency>
   <groupId>btrplace</groupId>
   <artifactId>btrpsl</artifactId>
   <version>1.1</version>
</dependency>
```

## Documentation ##

* Javadoc for the last snapshot version: http://btrp.inria.fr:8080/apidocs/snapshots/btrpsl
* Javadoc for the released versions: http://btrp.inria.fr:8080/apidocs/releases/btrplace/btrpsl

## Building from sources ##

Requirements:
* JDK 6+
* maven 3+

The source of the released versions are directly available in the `Tag` section.
You can also download them using github features.
Once downloaded, move to the source directory then execute the following command
to make the jar:

    $ mvn clean install

If the build succeeded, the resulting jar will be automatically
installed in your local maven repository and available in the `target` sub-folder.

Copyright
-------------------------------
Copyright (c) 2013 University of Nice-Sophia Antipolis. See `LICENSE.txt` for details
