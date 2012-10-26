source("global.R")
FOLDER = "rf-nn-09-0.9-0.1/"
INDEX = "0"

# output
postscript(file="fig.eps", onefile=FALSE, horizontal=FALSE, width=5, height=8, paper="special")
#png(file="fig/crash_scale.png", width=5000, height=2000)

# read data
d <- read.table("/Users/chi/Desktop/maxc.txt", header=F, sep="\t")
freq <- NA
crash <- NA
for (i in 1 : NROW(d)) {
  for (j in 2 : NCOL(d)) {
    freq <- c(freq, d[i, 1])
    crash <- c(crash, d[i,j])
  }
}
max <- data.frame(freq, crash)


d <- read.table("/Users/chi/Desktop/minc.txt", header=TRUE, sep="\t")
freq <- NA
crash <- NA
for (i in 1 : NROW(d)) {
  for (j in 2 : NCOL(d)) {
    freq <- c(freq, d[i, 1])
    crash <- c(crash, d[i,j])
  }
}
min <- data.frame(freq, crash)


# plot settings
par(oma=c(0,0,0,0))        # Trim off excess outer margin space (bottom, left, top, right)
#par(mgp=c(1.9,0.6,0))      # Trim off excess space for label and ticks (label, ticks, line)
par(mfrow=c(2,1))          # (rows, cols)
#par(mar=c(3,2.9,0.2,4))

# plot max crashes
boxplot(max$crash~max$freq, range=0, xlab=expression(lambda[mu]), ylab="price movement (ratio)", main="Maximum crash scale")

# plot min crashes
boxplot(min$crash~min$freq, range=0, xlab=expression(lambda[mu]), ylab="price movement (ratio)", main="Minimum crash scale")

dev.off()