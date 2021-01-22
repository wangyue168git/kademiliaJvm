package kademlia;

import kademlia.config.Config;
import kademlia.node.KademliaId;
import kademlia.node.Node;
import kademlia.util.FileHashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author wesleywang
 * @Description:
 * @date 2021/1/22
 */
@Service
@Slf4j
public class KadService {

    @Autowired
    private Config config;

    private JKademliaNode node;

    @PostConstruct
    private void create(){
        try {
            log.info("种子值：" + config.getUser());
            if (config.getUser().equals("seedNode0")){
                node = new JKademliaNode(config.getUser(), new KademliaId(
                        FileHashUtil.sha1Hash("49.234.126.59".getBytes())), 20300);
                log.info("seedNode0 start success ! ! !");
            }else {
                node = new JKademliaNode(config.getUser(), new KademliaId(
                        FileHashUtil.sha1Hash(getLocalMac().getBytes())), 20300);
                Node seed = new Node(new KademliaId(FileHashUtil.sha1Hash("49.234.126.59".getBytes())),
                        InetAddress.getByName("49.234.126.59"), 20300);
                node.bootstrap(seed);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getLocalMac() throws SocketException, UnknownHostException {
        //获取网卡，获取地址
        InetAddress ia = InetAddress.getLocalHost();
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        StringBuffer sb = new StringBuffer("");
        for(int i=0; i<mac.length; i++) {
            if(i!=0) {
                sb.append("-");
            }
            //字节转换为整数
            int temp = mac[i]&0xff;
            String str = Integer.toHexString(temp);
            if(str.length()==1) {
                sb.append("0"+str);
            }else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }

    public JKademliaNode getNode() {
        return node;
    }

    public String getUser(){
        return config.getUser();
    }
}
