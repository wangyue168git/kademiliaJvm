package kademlia.file;

import com.google.gson.Gson;
import kademlia.dht.KadContent;
import kademlia.node.KademliaId;

import java.util.List;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/1/21
 */
public class FileIndex implements KadContent {

    public static final transient String TYPE = "FileIndex";

    private KademliaId key;
    private List<KademliaId> ids;
    private String fileName;
    private String ownerId;
    private final long createTs;
    private long updateTs;

    {
        this.createTs = this.updateTs = System.currentTimeMillis() / 1000L;
    }

    public FileIndex() {

    }

    public FileIndex(KademliaId key, List<KademliaId> ids, String fileName, String ownerId) {
        this.key = key;
        this.ids = ids;
        this.fileName = fileName;
        this.ownerId = ownerId;
    }



    @Override
    public KademliaId getKey() {
        return key;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public long getCreatedTimestamp() {
        return createTs;
    }

    @Override
    public long getLastUpdatedTimestamp() {
        return updateTs;
    }

    @Override
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Set the content as updated
     */
    public void setUpdated() {
        this.updateTs = System.currentTimeMillis() / 1000L;
    }


    @Override
    public byte[] toSerializedForm() {
        Gson gson = new Gson();
        return gson.toJson(this).getBytes();
    }

    @Override
    public FileIndex fromSerializedForm(byte[] data) {
        Gson gson = new Gson();
        return gson.fromJson(new String(data), FileIndex.class);
    }

    @Override
    public String toString() {
        return "FileIndex[{data size=" + this.ids.size() + "{ {key:" + this.key + "}]";
    }

    public List<KademliaId> getIds() {
        return ids;
    }

    public void setIds(List<KademliaId> ids) {
        this.ids = ids;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
