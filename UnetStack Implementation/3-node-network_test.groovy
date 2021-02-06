//! Simulation: Simple 3-node network
import org.arl.fjage.*

println '''
3-node network
--------------
'''

platform = RealTimePlatform

// run the simulation forever
simulate {
  
  node 'B', address: 101, location: [ 0.km, 0.km, -15.m], web:8081,api: 1101, shell: true, stack: "$home/etc/setup2"
  
  node '1', address: 102, location: [ 0.km, 1.km, -15.m], web:8082,api: 1102, shell: 5101, stack: "$home/etc/setup"
  node '2', address: 103, location: [-900.m, 0.km, -15.m], web:8083,api: 1103, shell: 5102, stack: "$home/etc/setup"
  node '3', address: 104, location: [ 1.5.km, 0.km, -15.m], web:8084,api: 1104, shell: 5103, stack: "$home/etc/setup"

}
