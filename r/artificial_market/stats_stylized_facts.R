source("~/git/artificial_market/r/artificial_market/global.R")
library("PerformanceAnalytics")
library("R.utils")
MM_FOLDER = "EXP-01/rf-sm-nn-10-%.1f-0.00/"
#FOLDER = "CLB-03.1/rf-pm-nn-00-0.9-0.1x10.9/"
SIMULATION_RUN = 100
lambda_set <- c(0.2, 0.4, 0.6, 0.8, 1.0)

# bulk process
d <- dataFrame(nrow=SIMULATION_RUN, colClasses=c(
  r_mu="double",
  r_me="double",
  r_max="double",
  r_min="double",
  r_sd="double",
  r_sk="double",
  r_kt="double",
  r_acf="double",
  abs_r_acf="double"
))
atom2 <- data.frame()

for (j in 1 : length(lambda_set)) {
  if (exists("MM_FOLDER")) {
    FOLDER = sprintf(MM_FOLDER, lambda_set[j])
  }
  for (i in 1 : SIMULATION_RUN) {
    cat(sprintf("%d.%d ", j, i))
    
    # log return
    FILE = 'marketlt'
    mkt <- read.table(paste(ROOT, FOLDER, FILE, i-1, sep=""), header=TRUE, sep=",")
    mkt <- subset(mkt, t > TRAINING_T)
    #mkt <- mkt[complete.cases(mkt), ]  # MM
    #d$spread[i] <- mean(mkt$spread)    # MM
    
    r <- logReturn(mkt$p_lt)
    d$r_mu[i] <- mean(r)
    d$r_me[i] <- median(r)
    d$r_max[i] <- max(r)
    d$r_min[i] <- min(r)
    d$r_sd[i] <- sd(r)
    d$r_kt[i] <- kurtosis(r)
    d$r_sk[i] <- skewness(r)
    d$r_acf[i] <- acf(r, plot=FALSE)$acf[2]
    d$abs_r_acf[i] <- acf(abs(r), plot=FALSE)$acf[2] 
  }
  # report
  d2 <- d[complete.cases(d), ]
  atom <- lapply(d2, median)
  #atom$pos_r_max[1] <- max(d$pos_r_max)
  #atom$neg_r_max[1] <- max(d$neg_r_max)
  atom2 <- rbind(atom2, atom)
}

write.table(atom2, file="artificial_market/zout", sep=",", row.names=F, col.names=T)
