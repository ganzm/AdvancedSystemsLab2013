package ch.ethz.mlmq.server.db.util;

public class StoredProcedureScriptRunner extends ScriptRunner {

	@Override
	protected String[] split(String fileContentString) {
		return fileContentString.split("----");
	}
}
