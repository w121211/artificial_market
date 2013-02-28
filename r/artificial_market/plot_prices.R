source("~/git/artificial_market/r/artificial_market/global.R")
NO_MM_FOLDER = 'EXP-03.1/rf-nn-nn-00-0.0-0.00/'
MM_FOLDER = 'EXP-03.1/rf-sm-nn-10-%.1f-0.00/'
FILE = "marketlt"
INDEX = "89"
if (F) {
  folder_set <- c(
    "EXP-01/rf-pm-nn-10-1.0-0.00/",
    "EXP-02/rf-pm-nn-10-1.0-0.00/")
  title_set <- c(
    "Experiment 1",
    "Experiment 2")
} else {
  folder_set <- c("EXP-03.1/rf-sm-nn-10-1.0-0.00-1-50-0.0020/")
  title_set <- c("")
}

# output
postscript(file="artificial_market/fig/prices.eps", onefile=FALSE, horizontal=FALSE, width=5, height=4, paper="special")
#png(file=paste("fig/", chartr(old="/",new="-",x=FOLDER), INDEX, ".png", sep=""), width=5000, height=2000)

#par(mar=c(3,2.9,0.2,4))
#par(oma=c(0,0,0,0))        # Trim off excess outer margin space (bottom, left, top, right)
#par(mgp=c(1.9,0.6,0))      # Trim off excess space for label and ticks (label, ticks, line)
par(mfrow=c(length(folder_set), 1))          # (rows, cols)

col_set <- c(rainbow(length(folder_set)))
for (i in 1 : length(folder_set)) {
  folder = folder_set[i]
  d <- read.table(paste(ROOT, folder, FILE, INDEX, sep=""), header=TRUE, sep=",")
  
  # plot
  t <- range(d$lt)
  price <- range(c(d$p_f_t, d$p_lt))
  plot(t, price, type="n", xlab="T", main=title_set[i])
  grid()
  lines(d$lt, d$p_lt, type="l", col="red")
  lines(d$lt, d$p_f_t, type="l", col="blue", lty=2)
  legend("topright", col=c("red","blue"), lty=c(1,2), legend=c(
    expression(p[T]), expression(p[T]^f)), cex=.8)
}

dev.off()
