source("~/git/artificial_market/r/artificial_market/global.R")
#FOLDER = "EXP-04.1/rf-pm-nn-10-1.0-0.00x5/"
FOLDER = "EXP-02/rf-Sm-nn-10-1.0-0.00/"
FILE = "marketlt"
INDEX = "74"

# output
postscript(file="artificial_market/fig/prices.eps", onefile=FALSE, horizontal=FALSE, width=5, height=8, paper="special")
#png(file=paste("fig/", chartr(old="/",new="-",x=FOLDER), INDEX, ".png", sep=""), width=500, height=200)

# read data
d <- read.table(paste(ROOT, FOLDER, FILE, INDEX, sep=""), header=TRUE, sep=",")

# plot 
#par(mar=c(3, 2.9, 0.2, 3)) # Trim off excess margin space (bottom, left, top, right)
par(oma=c(0,0,0,0))        # Trim off excess outer margin space (bottom, left, top, right)
par(mgp=c(1.9,0.6,0))      # Trim off excess space for label and ticks (label, ticks, line)
par(mar=c(3,2.9,0.2,4))
par(mfrow=c(3,1))          # (rows, cols)

# plot p_f_t, p_t
if (T) {
  if (F) {
    t <- range(d$lt)
    price <- range(d$p_lt, d$p_f_t)
  } else {
    t <- range(6500:8500)
    price <- range(d$p_lt, d$p_f_t)
    #price <- range(83:100)
  }
  plot(t, price, type="n", xlab="")
  grid()
  lines(d$lt, d$p_lt, type="l", col="red")
  lines(d$lt, d$p_f_t, type="l", col="blue", lty=2)
  legend("topright", col=c("red","blue"), lty=c(1,2), 
         legend=c(expression(p[T]), expression(p[T]^f)), cex=.8)
}

# plot hft_freq, hft_wealth
if (F) {
  frequency <- c(0, 1)
  wealth <- range(d$hft_wealth_mu)
  plot(t, frequency, type="n", xlab="T")
  grid()
  lines(d$lt, d$hft_freq_mu, type="l", col="red")
  par(new=TRUE)
  plot(t, wealth,, type="n", xaxt="n", yaxt="n", xlab="", ylab="")
  axis(4)
  mtext("wealth", side=4, line=3)
  lines(d$lt, d$hft_wealth_mu, type="l", col="blue", lty=3)
  legend("bottomleft", col=c("red","blue"), lty=c(1,3), 
         legend=c("frequency","wealth"), cex=.8)
}

# plot traders position
if (F) {
  #pos <- range(c(d$hft_pos,d$lft_pos))
  pos <- range(c(d$lft_pos))
  #pos <- range(-1000:1000)
  plot(t, pos, type="n", xlab="")
  grid()
  lines(d$t, d$t*0, type="l", col="#000000")
  lines(d$t, d$hft_pos, type="l", col="#cc0000")
  lines(d$t, d$lft_pos, type="l", col="#009999")
  legend("topleft", col=c("#cc0000","#009999"), lty=1, legend=c("HFT","LFT"), cex=.8)
}


# plot market depth
if (T) {
  FILE = "orderbook"
  d <- read.table(paste(ROOT, FOLDER, FILE, INDEX, sep=""), header=TRUE, sep=",")
  #volume <- range(-500:500)
  volume <- range(c(d$lsa, d$lba*-1))
  plot(t, volume, type="n", xlab="", ylab="market depth (shares)")
  grid()
  lines(d$lt, d$lt*0, type="l", col="#000000")
  lines(d$lt, d$lsa, type="l", col="#00cc99")
  lines(d$lt, d$lba*-1, type="l", col="#009999", lty=2)
  legend("topleft", col=c("#00cc99","#009999"), lty=c(1,2), 
         legend=c("stylized traders sell","stylized traders buy"), cex=.8)
  
  volume <- range(c(d$hsa, d$hba*-1))
  #volume <- range(-10:10)
  plot(t, volume, type="n", xlab="T", ylab="market depth (shares)")
  grid()
  lines(d$lt, d$lt*0, type="l", col="#000000")
  lines(d$lt, d$hsa, type="l", col="#ff9900")
  lines(d$lt, d$hba*-1, type="l", col="#ff6600", lty=2)
  legend("topleft", col=c("#ff9900","#ff6600"), lty=c(1,2), 
         legend=c("market makers sell","market makers buy"), cex=.8)
}

dev.off()