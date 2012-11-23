source("global.R")

# output
postscript(file="fig.eps", onefile=FALSE, horizontal=FALSE, width=5, height=8, paper="special")
#png(file="fig/crash_scale.png", width=5000, height=2000)

# read data
d <- read.table("/Users/chi/Desktop/cs.txt", header=T, sep="\t")

# plot settings
par(oma=c(0,0,0,0))        # Trim off excess outer margin space (bottom, left, top, right)
#par(mgp=c(1.9,0.6,0))      # Trim off excess space for label and ticks (label, ticks, line)
par(mfrow=c(2,1))          # (rows, cols)
#par(mar=c(3,2.9,0.2,4))

# plot max spikes
boxplot(d$max~d$lambda, range=0, xlab=expression(lambda[mu]), ylab="price movement (ratio)", main="Maximum spike scale")

# plot max crashes
boxplot(abs(d$min)~d$lambda, range=0, xlab=expression(lambda[mu]), ylab="price movement (ratio)", main="Maximum crash scale")

dev.off()