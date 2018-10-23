package cn.com.tw.common.web.anno.module;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import cn.com.tw.common.web.api.MachineController;
import cn.com.tw.common.web.listener.pubc.InitListener;
import cn.com.tw.common.web.utils.gener.NumberGenerator;

public class CusModuleImportSelector implements DeferredImportSelector {
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[] {InitListener.class.getCanonicalName(),NumberGenerator.class.getCanonicalName(),
				MachineController.class.getCanonicalName()};
	}
}