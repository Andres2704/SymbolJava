package lambdacloud.core.lang;

import symjava.symbolic.TypeInfo;

public class LCByte extends LCVar {
	public LCByte(String name) {
		super(name);
	}

	@Override
	public TypeInfo getTypeInfo() {
		return TypeInfo.tiByte;
	}
}
