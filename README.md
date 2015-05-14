# PlanetSim

PlanetSim is an object oriented simulation framework for overlay networks and services.
This framework presents a layered and modular architecture with well defined hotspots
documented using classical design patterns.

## Introduction

The PlanetSim is an overlay simulator. Exists a clear separation of
the three different layers that are simulated:

1. **Network level.** Mantain the communication between all nodes in the network.
1. **Node level:** Implements each desired overlay, consisting in
   its routing table and the application suppport ability.
1. **Application level.** Implements the desired (one or more per node)
   using the underlying node to contact with other applications.
   Each implemented application has an objective, implements
   a DHT (Distributed Hash Table), for example.

## Content

This distribution is a Java Eclipse project (http://eclipse.org).
One may use the "Import... -> Existing Project into Workspace"
functionallity and browsing to this uncompressed directory to load
the PlanetSim to your Eclipse.

Elsewhere, you may edit any class and add the required classes
and packages with any other IDE or editor. Then, you can use
the ant project manager to compile and build the distribution
planetsim.jar file automatically. See bin/README file for
more details.

Directory description:

* **bin/** Contain all required scripts and source data file to run the
   existing tests. Also contains the build.xml and
   planetsim.properties files to use Ant.
   See bin/README for more details.
* **docs/** Contain a brief introduction to the PlanetSim Architecture
   in HTML format. Also contain the HTML Javadoc API of the
   entire PlanetSim.
* **lib/** Contain the required jars to compile and run all tests.
* **src/** Contain the Java source classes of the PlanetSim.
* **out/** Contain the Java bytecode classes of the PlanetSim.
   Initially empty.
* **conf/** Contain the configuration files to run all the tests.

## Feedback

You can make a very good feedback send us your suggestions,
implemented extensions, fixed bugs or the errors.
To do so, report them as issues in [Planetsim github project](https://github.com/jpahullo/planetsim)

On any error or problem cases, you have to send these information:

1. Version of Planetsim in use
2. Short description of the error (or problem)
3. The detail of the main classes that take part (executed test, etc.)
4. The related stack trace (if exists)
5. Any other remarkable information.

## Developed under

This software was developed under a research project called
[Planet](http://ast-deim.urv.cat/web/projects/more-projects?view=project&task=show&id=3),
in the research [Group AST](http://ast-deim.urv.cat/) at [Universitat Rovira i Virgili](http://wwww.urv.cat)
and delivered on July 20, 2005 as Planetsim v3.0.

## Support

Authors move this project from Sourceforge to Github in spirit of sharing. Authors do no expect to provide an active support to this project.

## Authors

Pedro García López <pedro.garcia@urv.cat>

Carles Pairot Gavaldà <carles.pairot@urv.cat>

Rubén Mondéjar Andreu <ruben.mondejar@urv.cat>

Jordi Pujol Ahulló <jordi.pujol@urv.cat>

## License

PlanetSim is under GPL Licence v3. See LICENCE

Disclaimer: License was moved from LGPL to GPL v3 when moved to github site on May 14, 2015.

