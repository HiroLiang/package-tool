package com.example.demo.model.enumerate;

public enum CmdLineProcess {
	
	SEND_CLIENT("send-client");

	private final String option;

	CmdLineProcess(String option) {
		this.option = option;
	}
	
	public String getOption() {
        return option;
    }
	
	public static CmdLineProcess fromOption(String option) {
		if(option == null)
			return null;
		for (CmdLineProcess process : CmdLineProcess.values()) {
			if (process.getOption().equals(option)) {
				return process;
			}
		}
		throw new IllegalArgumentException("No such option: " + option);
	}

}
