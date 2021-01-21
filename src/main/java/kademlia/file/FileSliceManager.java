package kademlia.file;

import kademlia.JKademliaNode;
import kademlia.dht.GetParameter;
import kademlia.dht.KademliaStorageEntry;
import kademlia.exceptions.ContentNotFoundException;
import kademlia.node.KademliaId;
import kademlia.util.BufferedRandomAccessFile;
import kademlia.util.FileHashUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/1/21
 */
public class FileSliceManager {

    public static final int sliceLength = 1024 * 16;

    public static FileContent slice(String filePath) throws IOException {
        File file = new File(filePath);
        long length = file.length();
        FileContent fileContent = new FileContent();
        int index = 0;
        FileInputStream inputStream = new FileInputStream(file);
        //分多次将一个文件读入
        while (length > 0) {
            byte[] buffer;
            if (length <= sliceLength) {
                buffer = new byte[(int) length];
                length -= length;
            }else {
                buffer = new byte[sliceLength];
                length -= sliceLength;
            }

            if (inputStream.read(buffer, 0, buffer.length) != -1) {
                FileBlock fileBlock = new FileBlock();
                fileBlock.setIndex(index++);
                fileBlock.setData(buffer);
                fileBlock.setLength(buffer.length);
                fileBlock.setKademliaId(new KademliaId(FileHashUtil.sha1Hash(buffer)));
                fileContent.addFileBlock(fileBlock);
                continue;
            }
            break;
        }
        byte[] fileKey = FileHashUtil.shaHashCode(inputStream);
        if (fileKey == null){
            throw new RuntimeException("FileHashUtil.shaHashCode wrong");
        }
        fileContent.setKademliaId(new KademliaId(fileKey));
        String fileName = file.getName();
        fileContent.setFileName(fileName);
        inputStream.close();
        return fileContent;
    }

    public static void downLoadFile(KademliaId key, JKademliaNode node) throws IOException, ContentNotFoundException {
        GetParameter gp = new GetParameter(key, FileIndex.TYPE);
        KademliaStorageEntry index  = node.get(gp);
        FileIndex fileIndexContent = new FileIndex().fromSerializedForm(index.getContent());
        BufferedRandomAccessFile randomAccessFile = new BufferedRandomAccessFile(fileIndexContent.getFileName(),"rw");
        for (KademliaId id : fileIndexContent.getIds()) {
            GetParameter blockPara = new GetParameter(id, FileBlock.TYPE);
            try {
                KademliaStorageEntry entry  = node.get(blockPara);
                FileBlock block = new FileBlock().fromSerializedForm(entry.getContent());
                //文件写入指定位置
                randomAccessFile.seek(block.getIndex() * sliceLength);
                randomAccessFile.write(block.getData());
            } catch (IOException | ContentNotFoundException e) {
                e.printStackTrace();
            }
        }
        randomAccessFile.close();
    }

}
