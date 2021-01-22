package kademlia.simulations;

import kademlia.JKademliaNode;
import kademlia.file.FileContent;
import kademlia.file.FileIndex;
import kademlia.file.FileSliceManager;
import kademlia.node.KademliaId;

import java.io.IOException;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/1/21
 */
public class FileOperationTest {
    public static void main(String[] args)
    {
        try
        {
            /* Setting up 2 Kad networks */
            JKademliaNode kad1 = new JKademliaNode("JoshuaK", new KademliaId("ASF45678947584567467"), 8574);
            System.out.println("Created Node Kad 1: " + kad1.getNode().getNodeId());
            JKademliaNode kad2 = new JKademliaNode("Crystal", new KademliaId("ASERTKJDHGVHERJHGFLK"), 8572);
            System.out.println("Created Node Kad 2: " + kad2.getNode().getNodeId());
            kad2.bootstrap(kad1.getNode());

            String filePath = "/Users/wangyue/fisco/ipfs/kademiliaJvm/libs/gson-2.6.2.jar";
            FileContent content = FileSliceManager.slice(filePath);
            FileIndex fileShardContent = new FileIndex(content.getKademliaId(), content.getKadIds(),
                    content.getFileName(), kad2.getOwnerId());

            kad2.put(fileShardContent);
            content.getFileBlockList().forEach(fileBlock -> {
                fileBlock.setOwnerId(kad2.getOwnerId());
                try {
                    kad2.put(fileBlock);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            FileSliceManager.downLoadFile(fileShardContent.getKey(),kad1,"");
            System.out.println("文件下载成功");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
