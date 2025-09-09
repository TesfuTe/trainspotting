Train Control System Semaphores
This README outlines the semaphores used in Lab1.java to manage train movements in the Lab1.map railway system, ensuring collision-free operation. Each semaphore controls specific track sections, defined by associated sensors and switches, as detailed below.
Semaphore List

crossingLock

Role: Manages the four-way crossing to prevent collisions at the central intersection.
Sensors: (6,6), (8,6), (9,8), (10,7)
Switches: None


lowerTPathLock

Role: Controls the lower path (row 11) at the T-crossing, starting locked (0 permits).
Sensors: (14,7), (19,8)
Switches: (17,7)


upperTPath

Role: Manages the upper path (row 9) at the T-crossing, starting available (1 permit).
Sensors: (16,8), (19,8)
Switches: (17,7)


tCrossingMerge

Role: Protects the merge point at the T-crossing where upper and lower paths converge.
Sensors: (17,9), (13,9), (13,10)
Switches: (15,9)


upperMidPath

Role: Controls the upper middle path (row 9) between the T-crossing and west merge.
Sensors: (13,9), (6,9)
Switches: (15,9), (4,9)


lowerMidPath

Role: Manages the lower middle path (row 10) between the T-crossing and west merge.
Sensors: (13,10), (6,10)
Switches: (15,9), (4,9)


westMergeLock

Role: Ensures exclusive access to the west merge area where tracks converge.
Sensors: (6,9), (6,10), (2,9)
Switches: (4,9), (3,11)


upperSouthPathLock

Role: Controls the upper south path (row 11) to the south station, starting locked (0 permits).
Sensors: (5,11)
Switches: (3,11)


lowerSouthPath

Role: Manages the lower south path (row 13) to the south station, starting available (1 permit).
Sensors: (4,13)
Switches: (3,11)