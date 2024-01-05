package com.utils.common.obj.dynamicbean;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class ClassTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        try {
            if (className.equals("com/obj/proxy/TestServiceImpl")) {
                System.out.println("class name: " + className);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
