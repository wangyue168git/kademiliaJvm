package kademlia.controller;

import kademlia.KadService;
import kademlia.exceptions.ContentNotFoundException;
import kademlia.file.FileContent;
import kademlia.file.FileIndex;
import kademlia.file.FileSliceManager;
import kademlia.node.KademliaId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.File;
import java.io.IOException;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/1/22
 */
@RestController
@RequestMapping("kad")
public class Api {

    @Autowired
    private KadService kadService;

    @ResponseBody
    @RequestMapping("/upload")
    public String upload(String filePath)
            throws IOException {
        FileContent content = FileSliceManager.slice(filePath);
        FileIndex fileShardContent = new FileIndex(content.getKademliaId(), content.getKadIds(),
                content.getFileName(), kadService.getUser());
        kadService.getNode().put(fileShardContent);

        content.getFileBlockList().forEach(fileBlock -> {
            fileBlock.setOwnerId(kadService.getUser());
            try {
                kadService.getNode().put(fileBlock);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return "上传成功 ! \n + 文件hash：" +  fileShardContent.getKey();
    }


    @ResponseBody
    @RequestMapping("/download")
    public String upload(String filePath, String hash)
            throws IOException, ContentNotFoundException {
        String file = FileSliceManager.downLoadFile(new KademliaId(hexToByteArray(hash)), kadService.getNode(),filePath);
        return "下载成功！\n 保存地址：" + filePath + File.separator + file;
    }


    public static byte hexToByte(String inHex){
        return (byte)Integer.parseInt(inHex,16);
    }

    public static byte[] hexToByteArray(String inHex){
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1){
            //奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {
            //偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2){
            result[j]=hexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }

}
