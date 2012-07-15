source("~/git/workspace/java/ArtificialMarket-Chi-201206/r/global.R")
FILE = "orderbook"
INDEX = "0"

# read data
d <- read.table(paste(ROOT, FOLDER, FILE, INDEX, sep=""), header=TRUE, sep=",")

# plot
#postscript(file="figure.eps", onefile=FALSE, horizontal=FALSE, width=5, height=5, paper="special")
par(mar=c(3, 2.9, 0.2, 3)) # Trim off excess margin space (bottom, left, top, right)
par(oma=c(0,0,0,0))        # Trim off excess outer margin space (bottom, left, top, right)
par(mgp=c(1.9,0.6,0))      # Trim off excess space for label and ticks (label, ticks, line)
#par(mar=c(4,4,2,5)+.1)

if (FALSE) {
  t <- range(d$t)
  v <- range(-50:50)
  plot(t, v, type="n", xlab="")
  lines(d$t, d$hs1, type="l", col="red")
  lines(d$t, d$ls1, type="l", col="blue")
  lines(d$t, d$hb1*-1, type="l", col="red")
  lines(d$t, d$lb1*-1, type="l", col="blue")
  legend("topright", col=c("red","blue"), lty=1, legend=c("hb1","lb1"), cex=.8)
}

if (TRUE) {
  t <- range(10000:40000)
  v <- range(-50:50)
  plot(t, v, type="n", xlab="")
  lines(d$t, d$t*0, type="l", col="#000000")
  lines(d$t, d$hs1, type="l", col="#cc0000")
  lines(d$t, d$ls1, type="l", col="#00cc99")
  lines(d$t, d$hb1*-1, type="l", col="#cc0000")
  lines(d$t, d$lb1*-1, type="l", col="#009999")
  legend("topright", col=c("red","blue"), lty=1, legend=c("hb1","lb1"), cex=.8)
}
