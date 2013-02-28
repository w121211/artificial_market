source("~/git/artificial_market/r/artificial_market/global.R")
library("PerformanceAnalytics")
library("R.utils")
NO_MM_FOLDER = 'EXP-03.3/rf-nn-nn-00-0.0-0.00/'
MM_FOLDER = 'EXP-02/rf-sm-nn-10-%.1f-0.00/'
SIMULATION_RUN = 100
f0 = "EXP-03.1/rf-nn-nn-00/"
f1 = "EXP-03.1/rf-sm-nn-10-1.0-0.00-1-5-0.0020/"
f2 = "EXP-03.1/rf-sm-nn-10-1.0-0.00-1-10-0.0020/"
f3 = "EXP-03.1/rf-sm-nn-10-1.0-0.00-1-25-0.0020/"
f4 = "EXP-03.1/rf-sm-nn-10-1.0-0.00-1-50-0.0020/"
f5 = "EXP-04.1/rf-sm-nn-10-1.0-0.00x10/"
folder_set <- c(f5)
#lambda_set <- c(0, 0.2, 0.4, 0.6, 0.8, 1.0)

# bulk process
d <- dataFrame(nrow=SIMULATION_RUN, colClasses=c(
  p_min="double",
  pos_r_max="double",
  neg_r_max="double",
  t_arrived="double",
  t_bottom="double"
))

e <- dataFrame(nrow=SIMULATION_RUN, colClasses=c(
  abs_r_mean="double",
  abs_r_sd="double"
))

atom <- data.frame()

for (j in 1 : length(folder_set)) {
  for (i in 1 : SIMULATION_RUN) {
    cat(sprintf("%d.%d ", j, i))
    
    # read data
    FILE = 'marketlt'
    folder <- folder_set[j]
    mkt <- read.table(paste(ROOT, folder, FILE, i-1, sep=""), header=TRUE, sep=",")
    mkt <- subset(mkt, lt > TRAINING_T)
    
    d$p_min[i] <- min(mkt$p_lt)
    d$t_bottom[i] <- which(mkt$p_lt == min(mkt$p_lt))[1]
    d$t_arrived[i] <- which(mkt$p_lt < 90)[1]
    #mkt <- subset(mkt, lt > TRAINING_T+d$bottom_t[i])
    #d$p_max[i] <- max(mkt$p_lt)
    
    r <- logReturn(mkt$p_lt)
    abs_r <- abs(r)
    e$abs_r_mean[i] <- mean(abs_r[abs_r>0])
    e$abs_r_sd[i] <- sd(abs_r[abs_r>0])
    d$pos_r_max[i] <- max(r)
    d$neg_r_max[i] <- min(r)
  }
  
  atom <- rbind(atom, data.frame(lapply(d, median)))
}

# report
#atom2 <- data.frame(lapply(e, mean))

write.table(atom, file="artificial_market/zout", sep=",", row.names=F, col.names=F)