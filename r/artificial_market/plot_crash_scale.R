source("~/git/artificial_market/r/artificial_market/global.R")
FILE = 'marketlt'
SIMULATION_RUN = 100
if (F) {
  f0 = "EXP-01/rf-nn-nn-00-0.0-0.00/"
  f1 = "EXP-01/rf-sm-nn-10-0.1-0.00/"
  f2 = "EXP-01/rf-sm-nn-10-0.2-0.00/"
  f3 = "EXP-01/rf-sm-nn-10-0.3-0.00/"
  f4 = "EXP-01/rf-sm-nn-10-0.4-0.00/"
  f5 = "EXP-01/rf-sm-nn-10-0.5-0.00/"
  f6 = "EXP-01/rf-sm-nn-10-0.6-0.00/"
  f7 = "EXP-01/rf-sm-nn-10-0.7-0.00/"
  f8 = "EXP-01/rf-sm-nn-10-0.8-0.00/"
  f9 = "EXP-01/rf-sm-nn-10-0.9-0.00/"
  f10 = "EXP-01/rf-sm-nn-10-1.0-0.00/"
  folder_set <- c(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10)
  x_set <- c(0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0)
  xlabel=expression(lambda)
} else if (F) {
  f0 = "EXP-01/rf-nn-nn-00-0.0-0.00/"
  f1 = "EXP-01/rf-pm-nn-10-0.1-0.00/"
  f2 = "EXP-01/rf-pm-nn-10-0.2-0.00/"
  f3 = "EXP-01/rf-pm-nn-10-0.3-0.00/"
  f4 = "EXP-01/rf-pm-nn-10-0.4-0.00/"
  f5 = "EXP-01/rf-pm-nn-10-0.5-0.00/"
  f6 = "EXP-01/rf-pm-nn-10-0.6-0.00/"
  f7 = "EXP-01/rf-pm-nn-10-0.7-0.00/"
  f8 = "EXP-01/rf-pm-nn-10-0.8-0.00/"
  f9 = "EXP-01/rf-pm-nn-10-0.9-0.00/"
  f10 = "EXP-01/rf-pm-nn-10-1.0-0.00/"
  folder_set <- c(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10)
  x_set <- c(0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0)
  xlabel=expression(lambda)
} else if (F) {
  f0 = "EXP-02/rf-nn-nn-00-0.0-0.00/"
  f1 = "EXP-02/rf-sm-nn-10-0.1-0.00/"
  f2 = "EXP-02/rf-sm-nn-10-0.2-0.00/"
  f3 = "EXP-02/rf-sm-nn-10-0.3-0.00/"
  f4 = "EXP-02/rf-sm-nn-10-0.4-0.00/"
  f5 = "EXP-02/rf-sm-nn-10-0.5-0.00/"
  f6 = "EXP-02/rf-sm-nn-10-0.6-0.00/"
  f7 = "EXP-02/rf-sm-nn-10-0.7-0.00/"
  f8 = "EXP-02/rf-sm-nn-10-0.8-0.00/"
  f9 = "EXP-02/rf-sm-nn-10-0.9-0.00/"
  f10 = "EXP-02/rf-sm-nn-10-1.0-0.00/"
  folder_set <- c(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10)
  x_set <- c(0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0)
  xlabel=expression(lambda)
} else if (F) {
  f0 = "EXP-02/rf-nn-nn-00-0.0-0.00/"
  f1 = "EXP-02/rf-pm-nn-10-0.1-0.00/"
  f2 = "EXP-02/rf-pm-nn-10-0.2-0.00/"
  f3 = "EXP-02/rf-pm-nn-10-0.3-0.00/"
  f4 = "EXP-02/rf-pm-nn-10-0.4-0.00/"
  f5 = "EXP-02/rf-pm-nn-10-0.5-0.00/"
  f6 = "EXP-02/rf-pm-nn-10-0.6-0.00/"
  f7 = "EXP-02/rf-pm-nn-10-0.7-0.00/"
  f8 = "EXP-02/rf-pm-nn-10-0.8-0.00/"
  f9 = "EXP-02/rf-pm-nn-10-0.9-0.00/"
  f10 = "EXP-02/rf-pm-nn-10-1.0-0.00/"
  folder_set <- c(f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10)
  x_set <- c(0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0)
  xlabel=expression(lambda)
} else if (T) {
  folder_set <- c(
    "EXP-02/rf-sm-nn-10-1.0-0.00/",
    "EXP-04.1/rf-sm-nn-10-1.0-0.00x5/",
    "EXP-04.1/rf-sm-nn-10-1.0-0.00x10/",
    "EXP-04.1/rf-sm-nn-10-1.0-0.00x20/",
    "EXP-04.1/rf-sm-nn-10-1.0-0.00x50/")
  x_set <- c(1, 5, 10, 20, 50)
  xlabel="minimum resting period (T)"
} else if (F) {
  folder_set <- c(
    "EXP-02/rf-pm-nn-10-1.0-0.00/",
    "EXP-04.1/rf-pm-nn-10-1.0-0.00x5/",
    "EXP-04.1/rf-pm-nn-10-1.0-0.00x10/",
    "EXP-04.1/rf-pm-nn-10-1.0-0.00x20/",
    "EXP-04.1/rf-pm-nn-10-1.0-0.00x50/")
  x_set <- c(1, 5, 10, 20, 50)
  xlabel="minimum resting period (T)"
}

# output
postscript(file="artificial_market/fig/crash_scale.eps", onefile=FALSE, horizontal=FALSE, width=6, height=8, paper="special")

# read data
d <- data.frame(t(rep(NA, 3)))
names(d) <- c("x", "pos_r_max", "neg_r_max")

for (i in 1: length(folder_set)) {
  folder <- folder_set[i]
  for (j in 1 : SIMULATION_RUN) {
    cat(sprintf("%d.%d ", i, j))
    filepath <- paste(ROOT, folder, FILE, j-1, sep="")
    mkt <- read.table(filepath, header=TRUE, sep=",")
    mkt <- subset(mkt, lt > TRAINING_T)
    
    r <- logReturn(mkt$p_lt)
    pos_r <- r[r>0]
    neg_r <- r[r<0]
    d <- rbind(d, c(x_set[i], max(pos_r), max(abs(neg_r))))
  }
}
d <- d[-1,]

# plot settings
par(oma=c(0,0,0,0))        # Trim off excess outer margin space (bottom, left, top, right)
par(mgp=c(1.9,0.6,0))      # Trim off excess space for label and ticks (label, ticks, line)
par(mfrow=c(2,1))          # (rows, cols)

# plot max spikes
boxplot(d$pos_r_max~d$x, range=0, xlab=xlabel, ylab="return", main="Dispersion of maximum spike scales")
# plot max crashes
boxplot(d$neg_r_max~d$x, range=0, xlab=xlabel, ylab="return", main="Dispersion of maximum crash scales")

dev.off()