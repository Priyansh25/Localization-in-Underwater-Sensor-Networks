//! Simulation

import org.arl.fjage.RealTimePlatform
import org.arl.unet.sim.*
import org.arl.unet.sim.channels.*

///////////////////////////////////////////////////////////////////////////////
// display documentation

println '''
AUV node network
------------------------------------------------------------

Node beacon1: tcp://localhost:1101, http://localhost:8081/
Node beacon2: tcp://localhost:1102, http://localhost:8082/
Node beacon3: tcp://localhost:1103, http://localhost:8083/
------------------------------------------------------------
Node Target: tcp://localhost:1104, http://localhost:8084/

------------------------------------------------------------
'''

///////////////////////////////////////////////////////////////////////////////
// simulation settings

platform = RealTimePlatform           // use real-time mode

///////////////////////////////////////////////////////////////////////////////
// channel settings

channel = [model: ProtocolChannelModel]

///////////////////////////////////////////////////////////////////////////////
// simulation details

simulate {
  node 'beacon1', address: 1, location: [0.km, 0.km,  -5.m], web: 8081, api: 1101, stack: "$home/scripts/setup.groovy", shell: true

  node 'beacon2', address: 2, location: [1.km, 0.km,  -30.m], web: 8082, api: 1102, stack: "$home/scripts/custom.groovy", shell: true

  node 'beacon3', address: 3, location: [1.5.km, 0.5.km,  -20.m], web: 8083, api: 1103, stack: "$home/scripts/custom.groovy", shell: true

  node 'target', address: 4, location: [1.km, 1.km,  -30.m], web: 8084, api: 1104,stack: "$home/scripts/custom.groovy", shell: true, mobility: true
  // auv.motionModel = [speed: 0.5.mps, turnRate:0.5.dps]
  
  //  def auv = node 'target', address: 4, location: [1.km, 1.km,  -30.m], stack: "$home/scripts/custom.groovy", shell: true, mobility: true
  //  auv.motionModel = [speed: 0.5.mps, turnRate:0.5.dps]
}