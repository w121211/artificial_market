source("~/git/artificial_market/r/artificial_market/global.R")
FILE = "marketlt"

# output
#postscript(file="artificial_market/fig/crashes.eps", onefile=FALSE, horizontal=FALSE, width=5, height=8, paper="special")
postscript(file="artificial_market/fig/crashes.eps", onefile=FALSE, horizontal=FALSE, width=4, height=2, paper="special")

if (F) {
  INDEX = "55" # crash
  folder_set <- c(
    "EXP-02/rf-pm-nn-10-1.0-0.00/",
    "EXP-04.1/rf-pm-nn-10-1.0-0.00x5/",
    "EXP-04.1/rf-pm-nn-10-1.0-0.00x10/",
    "EXP-04.1/rf-pm-nn-10-1.0-0.00x20/",
    "EXP-04.1/rf-pm-nn-10-1.0-0.00x50/")
  x_set <- c(1, 5, 10, 20, 50)
} else if (T) {
  INDEX = "0" # crash
  folder_set <- c(
    "EXP-02/rf-nn-nn-00-0.0-0.00/"
    #"EXP-02/rf-sm-nn-10-1.0-0.00/"
  )
  x_set <- c(0)
} else if (F) {
  folder_set <- c(
    "EXP-02/rf-sm-nn-10-0.1-0.00/",
    "EXP-04.1/rf-sm-nn-10-1.0-0.00x5/",
    "EXP-04.1/rf-sm-nn-10-1.0-0.00x10/",
    "EXP-04.1/rf-sm-nn-10-1.0-0.00x20/",
    "EXP-04.1/rf-sm-nn-10-1.0-0.00x50/")
  x_set <- c(1, 5, 10, 20, 50)
} else if (F) {
  folder_set <- c(
    "EXP-02/rf-nn-nn-00-0.0-0.00/",
    "EXP-02/rf-sm-nn-10-0.2-0.00/",
    "EXP-02/rf-sm-nn-10-0.4-0.00/",
    "EXP-02/rf-sm-nn-10-0.6-0.00/",
    "EXP-02/rf-sm-nn-10-0.8-0.00/",
    "EXP-02/rf-sm-nn-10-1.0-0.00/"
  )
  x_set <- c(0, 0.2, 0.4, 0.6, 0.8, 1.0)
} else if (F) {
  folder_set <- c(
    "EXP-02/rf-nn-nn-00-0.0-0.00/",
    "EXP-02/rf-pm-nn-10-0.2-0.00/",
    "EXP-02/rf-pm-nn-10-0.4-0.00/",
    "EXP-02/rf-pm-nn-10-0.6-0.00/",
    "EXP-02/rf-pm-nn-10-0.8-0.00/",
    "EXP-02/rf-pm-nn-10-1.0-0.00/"
  )
  x_set <- c(0, 0.2, 0.4, 0.6, 0.8, 1.0)
} else {
  folder_set <- c("EXP-03.1/rf-sm-nn-10-1.0-0.00-1-5-0.0020/",
                  "EXP-03.1/rf-sm-nn-10-1.0-0.00-1-10-0.0020/",
                  "EXP-03.1/rf-sm-nn-10-1.0-0.00-1-25-0.0020/",
                  "EXP-03.1/rf-sm-nn-10-1.0-0.00-1-50-0.0020/")
  x_set <- c(5,10,25,50)
}



#par(mar=c(3,2.9,0.2,4))
par(mar=c(2,2,1,0.2))
#par(oma=c(0,0,0,0))        # Trim off excess outer margin space (bottom, left, top, right)
#par(mgp=c(1.9,0.6,0))      # Trim off excess space for label and ticks (label, ticks, line)
#par(mfrow=c(length(folder_set), 1))          # (rows, cols)

for (i in 1 : length(folder_set)) {
  folder = folder_set[i]
  d <- read.table(paste(ROOT, folder, FILE, INDEX, sep=""), header=TRUE, sep=",")
  
  # plot
  t <- range(1000:10000)
  #t <- range(d$lt)
  #price <- range(c(d$p_f_t, d$p_lt))
  price <- range(c(90,115))
  #plot(t, price, type="n", xlab="")
  plot(t, price, type="n", xlab="", ylab="price", xaxt='n', ann=FALSE)
  grid()
  lines(d$lt, d$p_lt, type="l", col="red")
  lines(d$lt, d$p_f_t, type="l", col="blue", lty=2)
  #legend("topright", legend=bquote(period == .(x_set[i])*T), cex=.8)
  #legend("bottomright", legend=bquote(lambda == .(x_set[i])), cex=.8)
}
#title(xlab=expression(T))

dev.off()