package org.getchunky.chunkyvillage.objects;

import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunkyvillage.ChunkyTownManager;

public class TownChunk {

    private ChunkyChunk chunkyChunk;

    public TownChunk(ChunkyChunk chunkyChunk) {
        this.chunkyChunk = chunkyChunk;
    }

    public ChunkyChunk getChunkyChunk() {
        return chunkyChunk;
    }

    public void setForSale(double cost) {
        chunkyChunk.getData().put("cost", cost);
        chunkyChunk.save();
    }

    public void setNotForSale() {
        chunkyChunk.getData().remove("cost");
        chunkyChunk.save();
    }

    public boolean isForSale() {
        return chunkyChunk.getData().has("cost");
    }

    public double getCost() {
        return chunkyChunk.getData().getDouble("cost");
    }

    public boolean buyChunk(ChunkyResident buyer) {
        if(chunkyChunk.getOwner() instanceof ChunkyTown) {
            ChunkyTown chunkyTown = (ChunkyTown)chunkyChunk.getOwner();
            if(!chunkyTown.deposit(buyer,getCost())) return false;
            chunkyChunk.setOwner(buyer.getChunkyPlayer(),false,true);
            setNotForSale();
            Language.sendGood(buyer.getChunkyPlayer(), "You have purchased this chunk!");
            return true;
        }

        if(chunkyChunk.getOwner() instanceof ChunkyPlayer) {
            ChunkyResident seller = new ChunkyResident(chunkyChunk.getOwner());
            if(!seller.pay(buyer.getAccount(), getCost())) return false;
            chunkyChunk.setOwner(buyer.getChunkyPlayer(),false,true);
            setNotForSale();
            Language.sendGood(buyer.getChunkyPlayer(),"You have purchased this chunk!");
            return true;
        }
        return false;
    }

}
