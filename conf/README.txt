List of Attributes:

Factories attributes:
------------------------

Required:
New	- NetworkFactory implementation class
	- IdFactory implementation class
New	- NodeHandleFactory implementation class
	- NodeFactory implementation class
New	- RouteMessagePool implementation class
	- Network implementation class
	- NodeHandle implementation class
	- RouteMessage implementation class
	- Network topology
	- Network size

Optionals:
New	- ApplicationFactory implementation class
New	- EndPointFactory implementation class
	- Application implementation class
	- EndPoint implementation class

Simulator attributes:
-------------------------

Required:
	- Default simulation steps
	- Log level
New	- Simulation environment: simulation (by steps) or experimental (by time): It permits to select the required TimerTask in any Node
	- Maximum message queue size
	- Maximum number of processed messages per step

Optional:
	- Events filename

Serialization attributes: (test dependant)
-------------------------
	- Serialized file name
	- Output filename to serialize the current network
	- Replace output filename
	

Behaviours attributes: (overlay dependant)
-------------------------

Required:
	- Behaviours factory implementation class
	- Behaviours pool implementation class
	- Behaviours role selector implementation class
	- Behaviours invoker implementation class
	- Behaviours filter implementation class
	- Pattern implementation class for behaviour selection
	- Percentage of faulty nodes
	- Distribution of malicious nodes
	- Flag to show debug info on behaviours applying

Overlay attributes:
-------------------------

Required:
	- Id implementation class
	- Node implementation class
New	- With behaviours flag
	- Specific overlay properties implementation (i.e. number of bits per key, ...)


New:
--------------------->>>>>>>

Results attributes: (test dependant)
--------------------------

Required:
	- Results factory implementation class
	- Edge implementation class
	- Constraint implementation class
	- Results generator implementation class
	- Specific result properties implementation class (this class includes all specific attributes for each result type)
	- Unique names of available results type (i.e. GML, PAJEK, XML,....)

