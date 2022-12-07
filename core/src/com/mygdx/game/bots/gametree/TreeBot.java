package com.mygdx.game.bots.gametree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import com.mygdx.game.bots.Bot;
import com.mygdx.game.bots.FitnessGroupBot;
import com.mygdx.game.bots.MaxN_Paranoid_Bot;
import com.mygdx.game.bots.OLABot;
import com.mygdx.game.bots.RandomBot;
import com.mygdx.game.coordsystem.Hexagon;
import com.mygdx.game.scoringsystem.ScoringEngine;

public class TreeBot extends Bot {

    private Hexagon.state playerstate;
    private Hexagon.state oppstate;

    private ScoringEngine SE;
    private Bot trainingbot;

    private int depthmax;
    private int depthlimit;

    private ArrayList<Root> rootsR;
    private ArrayList<Root> rootsB;

    private ArrayList<Root> optrootsR;
    private ArrayList<Root> optrootsB;

    public TreeBot(Hexagon.state playerstate, Hexagon.state oppstate){
        this.playerstate = playerstate;
        this.oppstate = oppstate;

        rootsR = new ArrayList<Root>();
        rootsB = new ArrayList<Root>();
        optrootsR = new ArrayList<Root>();
        optrootsB = new ArrayList<Root>();

        SE = new ScoringEngine();

        //TWO VARIABLES BELOW CAN BE MODIFIED
        trainingbot = new MaxN_Paranoid_Bot(oppstate,playerstate);
        depthmax = 2;
    }

    public void setDepthLimit(ArrayList<Hexagon> field) {
        int tiles=0;
        for(Hexagon h:field) {
            if(h.getMyState()==Hexagon.state.BLANK) {
                tiles++;
            }
        }

        tiles = tiles - (field.size()%4);
        depthlimit = (tiles/2)-1;

        if(depthmax<depthlimit) {
            depthlimit=depthmax;
        }
        //System.out.println("The depth limit is: "+depthlimit);
    }

    public void calculate(ArrayList<Hexagon> field) {
        Random r = new Random();
        int rnum;
        boolean turn1=true;
        boolean turn2=true;

        rootsR = new ArrayList<Root>();
        rootsB = new ArrayList<Root>();
        optrootsR = new ArrayList<Root>();
        optrootsB = new ArrayList<Root>();

        setDepthLimit(field);

        computeRoots(field, Hexagon.state.RED, playerstate);
        setRoots(playerstate);

        rnum = r.nextInt(optrootsR.size());
        for(Hexagon hex:field) {
            if(optrootsR.get(rnum).getRootQ()==hex.getQ()&&optrootsR.get(rnum).getRootR()==hex.getR()) {
                hex.setMyState(Hexagon.state.RED);
            }
        }


        computeRoots(field, Hexagon.state.BLUE, playerstate);
        setRoots(playerstate);

        rnum = r.nextInt(optrootsB.size());
        for(Hexagon hex:field) {
            if(optrootsB.get(rnum).getRootQ()==hex.getQ()&&optrootsB.get(rnum).getRootR()==hex.getR()) {
                hex.setMyState(Hexagon.state.BLUE);
            }
        }


        //printOptimalRoots(playerstate);
    }

    public void printOptimalRoots(Hexagon.state player) {
        System.out.println("For playing as the colour "+player);
        for(Root r:optrootsR) {
            System.out.println("Optimal Red places are: q "+r.getRootQ()+". r "+r.getRootR()+ ". WITH SCORE: "+r.getScore());
        }
        for(Root r:optrootsB) {
            System.out.println("Optimal Blue places are: q "+r.getRootQ()+". r "+r.getRootR()+ ". WITH SCORE: "+r.getScore());
        }
    }

    public void setRoots(Hexagon.state player) {
        int best = Integer.MIN_VALUE;
        int worst = Integer.MAX_VALUE;

        for(Root r:rootsR) {
            if(player==Hexagon.state.RED) {
                if (r.getScore()>best) {
                    best=r.getScore();
                }
            } else if(player==Hexagon.state.BLUE) {
                if(r.getScore()<worst) {
                    worst=r.getScore();
                }
            }
        }

        for(Root r:rootsR) {
            if(player==Hexagon.state.RED) {
                if(r.getScore()==best) {
                    optrootsR.add(r);
                }
            } else if(player==Hexagon.state.BLUE) {
                if(r.getScore()==worst) {
                    optrootsR.add(r);
                }
            }
        }

        //--

        for(Root r:rootsB) {
            if(player==Hexagon.state.BLUE) {
                if (r.getScore()>best) {
                    best=r.getScore();
                }
            } else if(player==Hexagon.state.RED) {
                if(r.getScore()<worst) {
                    worst=r.getScore();
                }
            }
        }

        for(Root r:rootsB) {
            if(player==Hexagon.state.BLUE) {
                if(r.getScore()==best) {
                    optrootsB.add(r);
                }
            } else if(player==Hexagon.state.RED) {
                if(r.getScore()==worst) {
                    optrootsB.add(r);
                }
            }
        }
    }

    public void computeRoots(ArrayList<Hexagon> field, Hexagon.state color, Hexagon.state player) {
        // Copy the current state of the board
        ArrayList<Hexagon> clone = new ArrayList<Hexagon>();
        try {
            for(Hexagon h : field) {
                clone.add(h.clone());
            }
        } catch (Exception e) {}


        for(Hexagon hc : clone) {
            if(hc.getMyState()==Hexagon.state.BLANK) {
                hc.setMyState(color);
                Root root = new Root(hc.getQ(),hc.getR(),hc.getMyState(),player);

                if(color==Hexagon.state.RED) {
                    rootsR.add(root);
                } else if(color==Hexagon.state.BLUE) {
                    rootsB.add(root);
                }

                if(depthlimit==0) {
                    SE.calculate(clone);
                    if(root.getRootState()==Hexagon.state.RED) {
                        root.addLeaf(SE.getRedScore());
                    } else if(root.getRootState()==Hexagon.state.BLUE) {
                        root.addLeaf(SE.getBlueScore());
                    }
                }

                ArrayList<Hexagon> clone2 = new ArrayList<Hexagon>();
                try {
                    for(Hexagon h : field) {
                        clone2.add(h.clone());
                    }
                } catch (Exception e) {}

                if(color==Hexagon.state.BLUE && depthlimit>0) {
                    recursion(clone2, 1, Hexagon.state.RED, player, root);
                } else if(color==Hexagon.state.RED && depthlimit>0) {
                    recursion(clone2, 1, Hexagon.state.BLUE, player, root);
                }

                hc.setMyState(Hexagon.state.BLANK);
            }
        }

    }

    public void recursion(ArrayList<Hexagon> recursionfield, int d, Hexagon.state color, Hexagon.state player, Root root) {
        if(d>depthlimit) return;

        if(color==Hexagon.state.RED) {
            trainingbot.calculate(recursionfield);
        }
        
        for(Hexagon hcc : recursionfield) {
            if(hcc.getMyState()==Hexagon.state.BLANK){
                hcc.setMyState(color);


                if(d==depthlimit) {
                    SE.calculate(recursionfield);
                    if(root.getRootState()==Hexagon.state.RED) {
                        root.addLeaf(SE.getRedScore());
                        return;
                    } else if(root.getRootState()==Hexagon.state.BLUE) {
                        root.addLeaf(SE.getBlueScore());
                        return;
                    }
                }

                ArrayList<Hexagon> clone3 = new ArrayList<Hexagon>();
                try {
                    for(Hexagon h : recursionfield) {
                        clone3.add(h.clone());
                    }
                } catch (Exception e) {}

                if(color==Hexagon.state.BLUE) {
                    recursion(clone3, d+1, Hexagon.state.RED, player, root);
                } else if(color==Hexagon.state.RED) {
                    recursion(clone3, d+1, Hexagon.state.BLUE, player, root);
                }

                hcc.setMyState(Hexagon.state.BLANK);
            }
        }

    }


}
