package com.dhht.client;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


public class MyProtocalCodecFactory   implements ProtocolCodecFactory {  
        private final MyProtocalEncoder encoder;  //编码
        private final MyProtocalDecoder decoder;  //解码
          
        public MyProtocalCodecFactory( ) {  
            encoder=new MyProtocalEncoder();  
            decoder=new MyProtocalDecoder();  
        }  
           
        public ProtocolEncoder getEncoder(IoSession session) {  
            return encoder;  
        }  
        public ProtocolDecoder getDecoder(IoSession session) {  
            return decoder;  
        }  
          
} 