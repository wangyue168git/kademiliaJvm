package kademlia.file;

import com.google.gson.Gson;
import kademlia.dht.KadContent;
import kademlia.node.KademliaId;
import kademlia.simulations.DHTContentImpl;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/1/21
 */
public class FileBlock implements Comparable<FileBlock>, KadContent {

    public static final transient String TYPE = "FileBlock";

    private String blockHash;

    private long length;

    private KademliaId kademliaId;

    private int index;

    private byte[] data;

    private String ownerId;

    private final long createTs;

    private long updateTs;

    {
        this.createTs = this.updateTs = System.currentTimeMillis() / 1000L;
    }


    public FileBlock() {}

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(FileBlock o) {
        return this.index - o.index;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        setUpdated();
    }

    public KademliaId getKademliaId() {
        return kademliaId;
    }

    public void setKademliaId(KademliaId kademliaId) {
        this.kademliaId = kademliaId;
    }

    @Override
    public KademliaId getKey() {
        return kademliaId;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getOwnerId()
    {
        return this.ownerId;
    }

    /**
     * Set the content as updated
     */
    public void setUpdated()
    {
        this.updateTs = System.currentTimeMillis() / 1000L;
    }

    @Override
    public long getCreatedTimestamp()
    {
        return this.createTs;
    }

    @Override
    public long getLastUpdatedTimestamp()
    {
        return this.updateTs;
    }

    @Override
    public byte[] toSerializedForm()
    {
        Gson gson = new Gson();
        return gson.toJson(this).getBytes();
    }

    @Override
    public FileBlock fromSerializedForm(byte[] data)
    {
        Gson gson = new Gson();
        FileBlock val = gson.fromJson(new String(data), FileBlock.class);
        return val;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
