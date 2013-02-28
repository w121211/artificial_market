source("~/git/artificial_market/r/artificial_market/global.R")
library("PerformanceAnalytics")
FOLDER = "EXP-02/rf-sm-nn-10-1.0-0.00/"

# bulk process
f_sd <- rep(NA, SIMULATION_RUN)     # freq sd
max_abs_pos_r <- c()
max_abs_neg_r <- c()

for (i in 1 : SIMULATION_RUN) {
  # read data
  FILE = 'marketlt'
  mkt <- read.table(paste(ROOT, FOLDER, FILE, i-1, sep=""), header=TRUE, sep=",")
  mkt <- mkt[c(0:-TRAINING_T),]
  cat("data:", i, "\n")
  
  # crashes
  r <- logReturn(mkt$p_lt) # log returns
  pos_r <- r[r>0]
  neg_r <- r[r<0]
  max_abs_pos_r <- c(max_abs_pos_r, max(abs(pos_r)))
  max_abs_neg_r <- c(max_abs_neg_r, max(abs(neg_r)))
}

# report
rpt <- data.frame(max_abs_pos_r, max_abs_neg_r)