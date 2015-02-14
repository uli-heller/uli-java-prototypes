def outSb = new StringBuilder();
def errSb = new StringBuilder();
def proc = args.execute();
proc.consumeProcessOutput(outSb, errSb);
proc.waitFor();
def result = new StringBuilder();
if (outSb) {
  result.append('out:\n').append(outSb);
}
if (errSb) {
  result.append('err:\n').append(errSb);
}
return result.toString();