import org.arl.fjage.*

println '''
3-node network
--------------
'''
platform = RealTimePlatform

simulate {
  
  node 'B', address: 101, location: [ -233.9.m, -22.654.m, -15.m], web:8081,api: 1101, shell: true, stack: "$home/etc/setup2"
  
  node 'n1', address: 102, location: [ -965.0032.m, 789.414.m, -15.m], web:8082,api: 1102, shell: 5101, stack: "$home/etc/setup3"
  node 'n2', address: 103, location: [  424.0145.m, 0.m,       -15.m], web:8083,api: 1103, shell: 5102, stack: "$home/etc/setup3"
  node 'n3', address: 104, location: [ -224.923.m, -872.564.m, -15.m], web:8084,api: 1104, shell: 5103, stack: "$home/etc/setup3"

}

