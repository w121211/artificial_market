source("global.R")
library("PerformanceAnalytics")
FOLDER = "rf-rr-09-0.9-0.1/"

#############
# crashes   #
#############
noContCrash <- function(t, p) {  
  steps = 1  # steps to determine
  # pect = CRASH_PECT    # crash percentage
  count = 0
  rtns <- c(0)
  for (i in 2 : length(p)) {
      rtn = (p[i] -  p[i-1]) / p[i-1]
      # cat(ret, "\n")
      rtns <- c(rtns, rtn)
  }
  return(rtns)
}

contCrash <- function(t, p) {
  init_p = p[1]
  steps = 1
  vSteps <- c()
  vScale <- c()
  for (i in 3 : length(p)) {
    pre = p[i-1] - p[i-2]
    cur = p[i] - p[i-1]
    dir = pre * cur
    
    if (dir >= 0) {
      # direction not change
      steps = steps + 1
    } else if (dir < 0) {
      # direction changed
      
      if (steps > 5) {
        scale = (p[i] - init_p) / init_p
        vSteps <- c(vSteps, steps)
        vScale <- c(vScale, scale)
        cat(steps, ":", scale, "\n")
      }
      # init variables
      init_p = p[i]
      steps = 1
    }
  }
  rst <- data.frame(vSteps, vScale)
  return(rst)
}

# bulk process
max_rtn  <- rep(NA, SIMULATION_RUN)     # max return
min_rtn  <- rep(NA, SIMULATION_RUN)     # max return

j = 5
for (i in 1 : SIMULATION_RUN) {
  # read data
  mkt <- read.table(paste(ROOT, FOLDER, FILE, i-1, sep=""), header=TRUE, sep=",")
  mkt <- mkt[c(0:-TRAINING_T),]
  
  # run analysing functions
  cat("data:", i, "\n")
  rtns  <- noContCrash(mkt$lt, mkt$p_lt)
  max_rtn[i] <- max(rtns)
  min_rtn[i] <- min(rtns)
  #plot(density(rst$vSteps))
  #plot(density(rst$vScale))
}
# write.table(rst, "crash_rst.txt", sep=",")
rst <- data.frame(max_rtn, min_rtn)