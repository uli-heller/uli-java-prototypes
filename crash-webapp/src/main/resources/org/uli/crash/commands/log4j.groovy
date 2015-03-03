import org.crsh.cli.Command;
import org.crsh.cli.Usage;
import org.crsh.cli.Option;
import org.crsh.cli.Required;
import org.crsh.command.InvocationContext;
import org.apache.log4j.Level;

@Usage("log4j")
class log4j {
  @Usage("Lists all log4j loggers")
  @Command
  void ls(InvocationContext<Map> context) {
    for (def logger : org.apache.log4j.LogManager.getCurrentLoggers()) {
        def map = [ 'name': logger.getName(), 'level':logger.getEffectiveLevel() ];
    	context.provide(map);
    }
  }

  @Usage("Sets the log level")
  @Command
  String setLogLevel(
    @Required
    @Usage("the name of the logger")
    @Option(names = ["n", "name"])
    String loggerName, 
    @Required
    @Usage("the new level of the logger")
    @Option(names = ["l", "level"])
    String logLevel) {
    def logger = org.apache.log4j.LogManager.getLogger(loggerName);
    if (logger == null) {
      return "Unable to find logger '" + loggerName + "'";
    } else {
      def level = Level.toLevel(logLevel);
      if (level == null) {
        return "Unable to find loglevel '" + logLevel + "'";
      } else {
        logger.setLevel(level);
        return "OK";
      }
    }
  }
}
