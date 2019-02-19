package cn.cubercsl.slidysim.results;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class Result {

    @Id
    private Long id;
    @NotNull
    private long time;
    @NotNull
    private int moves;

    private long timeStamp;

    @Generated(hash = 600314815)
    public Result(Long id, long time, int moves, long timeStamp) {
        this.id = id;
        this.time = time;
        this.moves = moves;
        this.timeStamp = timeStamp;
    }

    @Generated(hash = 1176609929)
    public Result() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getMoves() {
        return this.moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


}
