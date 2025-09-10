import TSim.*;

import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;
//Test comment to see if I can make changes
public class Lab1 {

    public Lab1(int speed1, int speed2) {
        TSimInterface tsi = TSimInterface.getInstance();
        tsi.setDebug(false);

        try {
            tsi.setSpeed(1, speed1);
            tsi.setSpeed(2, speed2);
            tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);

        } catch (CommandException e) {
            e.printStackTrace();    // or only e.getMessage() for the error
            System.exit(1);
        }

        Semaphore crossingLock = new Semaphore(1);
        Semaphore lowerTPathLock = new Semaphore(0);
        Semaphore upperTPath = new Semaphore(1);
        Semaphore tCrossingMerge = new Semaphore(1);
        Semaphore upperMidPath = new Semaphore(1);
        Semaphore lowerMidPath = new Semaphore(1);
        Semaphore westMergeLock = new Semaphore(1);
        Semaphore upperSouthPathLock = new Semaphore(0);
        Semaphore lowerSouthPath = new Semaphore(1);

        class Train implements Runnable {

            int id;
            int speed;
            // True = north
            boolean direction;

            public Train(int id, int speed, boolean dir) {
                this.id = id;
                this.speed = speed;
                this.direction = dir;
            }

            @Override
            public void run() {
                try {
                    while (!!!false) {
                        SensorEvent se = tsi.getSensor(id);

                        // Lower T-path past crossing
                        if (se.getXpos() == 6 && se.getYpos() == 6 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                System.out.println("crossingLock acquired! - " + crossingLock.availablePermits());
                                crossingLock.acquire();
                                tsi.setSpeed(id, speed);
                            // to North
                            } else {
                                System.out.println("crossingLock released! - " + crossingLock.availablePermits());
                                crossingLock.release();
                            }
                        }

                        // Lower T-path east
                        if (se.getXpos() == 14 && se.getYpos() == 7 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                System.out.println("tCrossingMerge acquired! - " + tCrossingMerge.availablePermits());
                                tCrossingMerge.acquire();
                                System.out.println("lowerTPathLock released! - " + lowerTPathLock.availablePermits());
                                lowerTPathLock.release();
                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
                                tsi.setSpeed(id, speed);
                            // to North
                            } else {
                                System.out.println("tCrossingMerge released! - " + tCrossingMerge.availablePermits());
                                tCrossingMerge.release();
                            }
                        }

                        // Upper T-path north crossing
                        if (se.getXpos() == 8 && se.getYpos() == 6 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                System.out.println("crossingLock acquired! - " + crossingLock.availablePermits());
                                crossingLock.acquire();
                                tsi.setSpeed(id, speed);
                            // to North
                            } else {
                                System.out.println("crossingLock released! - " + crossingLock.availablePermits());
                                crossingLock.release();
                            }
                        }

                        // Upper T-path south crossing
                        if (se.getXpos() == 9 && se.getYpos() == 8 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                System.out.println("crossingLock released! - " + crossingLock.availablePermits());
                                crossingLock.release();
                            // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                System.out.println("crossingLock acquired! - " + crossingLock.availablePermits());
                                crossingLock.acquire();
                                tsi.setSpeed(id, speed);
                            }
                        }

                        // T-crossing merge east crossing
                        if (se.getXpos() == 10 && se.getYpos() == 7 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                System.out.println("crossingLock released! - " + crossingLock.availablePermits());
                                crossingLock.release();
                            // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                System.out.println("crossingLock acquired! - " + crossingLock.availablePermits());
                                crossingLock.acquire();
                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Upper T-path east T-crossing
                        if (se.getXpos() == 16 && se.getYpos() == 8 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                System.out.println("tCrossingMerge acquired! - " + tCrossingMerge.availablePermits());
                                tCrossingMerge.acquire();
                                System.out.println("upperTPath released! - " + upperTPath.availablePermits());
                                upperTPath.release();
                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_LEFT);
                                tsi.setSpeed(id, speed);
                            // to North
                            } else {
                                System.out.println("tCrossingMerge released! - " + tCrossingMerge.availablePermits());
                                tCrossingMerge.release();
                            }
                        }

                        // T-crossing merge east
                        if (se.getXpos() == 19 && se.getYpos() == 8 && se.getStatus() == SensorEvent.ACTIVE && direction) {
                            // to South
                            tsi.setSpeed(id, 0);
                            if (upperTPath.tryAcquire()) {
                                System.out.println("upperTPath acquired! - " + upperTPath.availablePermits());
                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_LEFT);
                            } else {
                                System.out.println("lowerTPathLock acquired! - " + lowerTPathLock.availablePermits());
                                lowerTPathLock.acquire();
                                tsi.setSwitch(17, 7, TSimInterface.SWITCH_RIGHT);
                            }
                            tsi.setSpeed(id, speed);
                        }

                        // T-crossing merge south
                        if (se.getXpos() == 17 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                if (upperMidPath.tryAcquire()) {
                                    System.out.println("upperMidPath acquired! - " + upperMidPath.availablePermits());
                                    tsi.setSwitch(15, 9, TSimInterface.SWITCH_RIGHT);
                                } else {
                                    System.out.println("lowerMidPath acquired! - " + lowerMidPath.availablePermits());
                                    lowerMidPath.acquire();
                                    tsi.setSwitch(15, 9, TSimInterface.SWITCH_LEFT);
                                }
                                tsi.setSpeed(id, speed);
                            // to North
                            }
                        }

                        // Upper mid-path east
                        if (se.getXpos() == 13 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                System.out.println("tCrossingMerge released! - " + tCrossingMerge.availablePermits());
                                tCrossingMerge.release();
                            // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                System.out.println("tCrossingMerge acquired! - " + tCrossingMerge.availablePermits());
                                tCrossingMerge.acquire();
                                System.out.println("upperMidPath released! - " + upperMidPath.availablePermits());
                                upperMidPath.release();
                                tsi.setSwitch(15, 9, TSimInterface.SWITCH_RIGHT);
                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Lower mid-path east
                        if (se.getXpos() == 13 && se.getYpos() == 10 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                System.out.println("tCrossingMerge released! - " + tCrossingMerge.availablePermits());
                                tCrossingMerge.release();
                            // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                System.out.println("tCrossingMerge acquired! - " + tCrossingMerge.availablePermits());
                                tCrossingMerge.acquire();
                                System.out.println("lowerMidPath released! - " + lowerMidPath.availablePermits());
                                lowerMidPath.release();
                                tsi.setSwitch(15, 9, TSimInterface.SWITCH_LEFT);
                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Upper mid-path west
                        if (se.getXpos() == 6 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                System.out.println("westMergeLock acquired! - " + westMergeLock.availablePermits());
                                westMergeLock.acquire();
                                tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);
                                System.out.println("upperMidPath released! - " + upperMidPath.availablePermits());
                                upperMidPath.release();
                                tsi.setSpeed(id, speed);
                            } else {
                                System.out.println("westMergeLock released! - " + westMergeLock.availablePermits());
                                westMergeLock.release();
                            }
                        }

                        // Lower mid-path west
                        if (se.getXpos() == 6 && se.getYpos() == 10 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                System.out.println("westMergeLock acquired! - " + westMergeLock.availablePermits());
                                westMergeLock.acquire();
                                tsi.setSwitch(4, 9, TSimInterface.SWITCH_RIGHT);
                                tsi.setSpeed(id, speed);
                                System.out.println("lowerMidPath released! - " + lowerMidPath.availablePermits());
                                lowerMidPath.release();
                            } else {
                                System.out.println("westMergeLock released! - " + westMergeLock.availablePermits());
                                westMergeLock.release();
                            }
                        }

                        // West merge
                        if (se.getXpos() == 2 && se.getYpos() == 9 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                tsi.setSpeed(id, 0);
                                if (upperSouthPathLock.tryAcquire()) {
                                    System.out.println("upperSouthPathLock acquired! - " + upperSouthPathLock.availablePermits());
                                    tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
                                    tsi.setSpeed(id, speed);
                                } else {
                                    System.out.println("lowerSouthPath acquired! - " + lowerSouthPath.availablePermits());
                                    lowerSouthPath.acquire();
                                    tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
                                    tsi.setSpeed(id, speed);
                                }
                                tsi.setSpeed(id, speed);
                            } else {
                                tsi.setSpeed(id, 0);
                                if (upperMidPath.tryAcquire()) {
                                    System.out.println("upperMidPath acquired! - " + upperMidPath.availablePermits());
                                    tsi.setSwitch(4, 9, TSimInterface.SWITCH_LEFT);
                                    tsi.setSpeed(id, speed);
                                } else if (lowerMidPath.tryAcquire()) {
                                    System.out.println("lowerMidPath acquired! - " + lowerMidPath.availablePermits());
                                    tsi.setSwitch(4, 9, TSimInterface.SWITCH_RIGHT);
                                    tsi.setSpeed(id, speed);
                                }
                            }
                        }

                        // Upper south-path west
                        if (se.getXpos() == 5 && se.getYpos() == 11 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                System.out.println("westMergeLock released! - " + westMergeLock.availablePermits());
                                westMergeLock.release();
                            // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                System.out.println("westMergeLock acquired! - " + westMergeLock.availablePermits());
                                westMergeLock.acquire();
                                System.out.println("upperSouthPathLock released! - " + upperSouthPathLock.availablePermits());
                                upperSouthPathLock.release();
                                tsi.setSwitch(3, 11, TSimInterface.SWITCH_LEFT);
                                tsi.setSpeed(id, speed);
                            }
                        }

                        // Lower south-path west
                        if (se.getXpos() == 4 && se.getYpos() == 13 && se.getStatus() == SensorEvent.ACTIVE) {
                            // to South
                            if (!direction) {
                                System.out.println("westMergeLock released! - " + westMergeLock.availablePermits());
                                westMergeLock.release();
                            // to North
                            } else {
                                tsi.setSpeed(id, 0);
                                System.out.println("westMergeLock acquired! - " + westMergeLock.availablePermits());
                                westMergeLock.acquire();
                                tsi.setSwitch(3, 11, TSimInterface.SWITCH_RIGHT);
                                System.out.println("lowerSouthPath released! - " + lowerSouthPath.availablePermits());
                                lowerSouthPath.release();
                                tsi.setSpeed(id, speed);
                            }
                        }

                        /// North Station
                        if (se.getXpos() == 13 && (se.getYpos() == 5 || se.getYpos() == 3) && direction && se.getStatus() == SensorEvent.ACTIVE) {
                            tsi.setSpeed(id, 0);
                            sleep(2000);
                            speed = -speed;
                            tsi.setSpeed(id, speed);
                            direction = !direction;
                        }

                        /// South Station
                        if (se.getXpos() == 13 && (se.getYpos() == 11 || se.getYpos() == 13) && !direction && se.getStatus() == SensorEvent.ACTIVE) {
                            tsi.setSpeed(id, 0);
                            sleep(2000);
                            speed = -speed;
                            tsi.setSpeed(id, speed);
                            direction = !direction;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Thread train2 = new Thread(new Train(2, speed2, true));
        train2.start();
        Thread train1 = new Thread(new Train(1, speed1, false));
        train1.start();
    }
}