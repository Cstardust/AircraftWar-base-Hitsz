package edu.hitsz.reward;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.gameTemplate.Game;
import edu.hitsz.application.MusicThread;
import edu.hitsz.observer.Observer;
import edu.hitsz.observer.Subject;

import java.util.ArrayList;

/*
道具：炸药包
作用：打印
 */
public class BombReward extends AbstractReward implements Subject{
    private ArrayList<Observer> obs = new ArrayList<>();
    public void addObs(ArrayList<Observer> o){
        obs.addAll(o);
    }

    @Override
    public void dispatch(){
        for(Observer o:obs){
            Game.addScore(o.handler());
        }
    }

    @Override
    //  炸弹没用到heroAircraft
    public void takeEffect(HeroAircraft heroAircraft) {
        if(MusicThread.getIsMusic()){
            new MusicThread("src/videos/bomb_explosion.wav",false,false).start();
        }
        dispatch();
    }

    public BombReward(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

}
