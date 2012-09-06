Btrplace Specification Language
===============================
The btrplace specification language (btrpsl) allows to express constraints
related to the placement of virtual machines in a datacenters.

This language is dedicated to datacenters administrators and applications administrators
that use [Entropy](http://entropy.gforge.inria.fr) to manage their nodes and virtual machines.

Features
--------------------------------
This language massively relies on set theory and common naming conventions of the
VMs and the nodes to provide an concise definition of sets. Placement
constraints allow then describe the expected placement without having to burden about
the details.

Installation Notes
-------------------------------
Btrpsl works as a standalone application or as a plugin for [Entropy](http://entropy.gforge.inria.fr).

### Installation as a standalone application ###

`btrplint` aims to execute btrpsl scripts. To install
the application, just declare the environment variable `BTRP_HOME` pointing
to the installation directory of btrpsl. If the variable is not declared,
they the current working directory will be used. Just type `btrp` to get the
available command line options.

### Installation as a plugin for Entropy ###

btrpsl can be used as an input to declare vjobs into entropy. There is no need
to recompile entropy:

- put the jar 'btrpsl' in this 'jar' directory to the 'jar' directory of entropy.

- copy the sample 'btrpVjobs.properties' into the config directory of Entropy. You
can then specify the constraints to load and the paths to consider as possible
includes. By default, only the current working directory is scanned where
the builder is looking for vjobs description.

- in the config directory of Entropy. Append 'btrp' into the list of builders to load
and specify the location of the class (here, 'xml' and 'pbd' builders were already
declared).


    vjobBuilders.load=xml,pbd,btrp

    vjobBuilders.location.xml=entropy.vjob.builder.xml.XMLVJobBuilderBuilder
    vjobBuilders.location.pbd=entropy.vjob.builder.protobuf.ProtobufVJobBuilderBuilder
    vjobBuilders.location.btrp=btrpsl.BtrPlaceVJobBuilderBuilder

Entropy should now be able to load vjobs written in btrPlace once restarted.

Basic examples
--------------------------------

The folder `examples` contains some example scripts. Once installed,
just type the following command to execute the sample script `vappAH.btrp`:

    btrPlaceSL$ ./btrplint valid -I examples/includes examples/vappHA.btrp


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
    VM[11..20] : microInstance<destroyable>;
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


Copyright
-------------------------------
Copyright (c) 2011 Fabien Hermenier. See `LICENSE.txt` for details.