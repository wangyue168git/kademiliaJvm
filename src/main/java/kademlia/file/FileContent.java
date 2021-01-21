package kademlia.file;

import kademlia.node.KademliaId;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/1/21
 */
public class FileContent {

    private List<FileBlock> fileBlockList = new ArrayList<>();

    private KademliaId kademliaId;

    private String fileType;

    private String fileName;

    private List<KademliaId> kadIds = new ArrayList<>();

    public List<FileBlock> getFileBlockList() {
        return fileBlockList;
    }

    public void setFileBlockList(List<FileBlock> fileBlockList) {
        this.fileBlockList = fileBlockList;
    }

    public void addFileBlock(FileBlock fileBlock) {
        this.fileBlockList.add(fileBlock);
        this.kadIds.add(fileBlock.getKademliaId());
    }

    public KademliaId getKademliaId() {
        return kademliaId;
    }

    public List<KademliaId> getKadIds() {
        return kadIds;
    }

    public void setKademliaId(KademliaId kademliaId) {
        this.kademliaId = kademliaId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
