package com.dhht.mina.server;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * 自定义实体类加解码工具
 */
public class MyProtocalCodecFactory   implements ProtocolCodecFactory {  
        private final MyProtocalEncoder encoder;  //编码
        private final MyProtocalDecoder decoder;  //解码

        public MyProtocalCodecFactory(MyProtocalEncoder encoder,MyProtocalDecoder decoder) {  
            this.encoder = encoder;  
            this.decoder = decoder;  
        }  
           
        public ProtocolEncoder getEncoder(IoSession session) {  
            return encoder;  
        }  
        public ProtocolDecoder getDecoder(IoSession session) {  
            return decoder;  
        }  
          
} 