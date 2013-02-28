source("~/git/artificial_market/r/artificial_market/global.R")
#MM_FOLDER = "EXP-01/rf-pm-nn-10-%.1f-0.00/"
FOLDER = "EXP-02/rf-pm-nn-10-1.0-0.00/"
SIMULATION_RUN = 1
FILE="marketlt"
INDEX="74"

# read data
d <- read.table(paste(ROOT, FOLDER, FILE, INDEX, sep=""), header=TRUE, sep=",")

# cross correlation
c <- ccf(d$p_f_t, d$p_lt, lag.max=1000,  plot=FALSE) 
cor = c$acf[,,1] 
lag = c$lag[,,1] 
r = data.frame(cor,lag)
r_max = r[which.max(r$cor),]
cat("max:", r_max[,1], r_max[,2])

plot(r$lag, r$cor)