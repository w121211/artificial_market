source("~/git/artificial_market/r/global.R")
library("PerformanceAnalytics")
FOLDER = "rf-nn-09-0.9-0.1/"

#############
# functions #
#############
countCrash <- function(t, p) {  
  period = CRASH_STEP  # steps to determine
  pect = CRASH_PECT    # crash percentage
  count = 0
  
  for (i in 1 : (length(p) - period) ) {
    isFound = FALSE
    for (j in (i+1) : (i+period)) {
      if (!isFound) {
        ret = (p[j] -  p[i]) / p[i]
        if (ret > pect || ret < -pect) {
          count = count + 1
          isFound = TRUE
          cat(t[i], ret, "\n")
        }
      }
    }
  }
  return(count)
}

crashMargin <- function(t, p) {  
  period = CRASH_STEP  # steps to determine
  max <- c()
  min <- c()
  for (i in 1 : (length(p)-period+1) ) {
    rtns <- c()
    for (j in (i+1) : (i+period-1)) {
      rtn = (p[j] -  p[i]) / p[i]
      rtns <- c(rtns, rtn)
    }
    max <- c(max, max(rtns))
    min <- c(min, min(rtns))
  }
  return(c(max(max), min(min)))
}

logReturn <- function(p) {
  r <- rep(NA, length(p)-1)
  for (i in 1 : length(r)) {
    r[i] = log(p[i+1] / p[i])
  }
  return (r)
}

absLogReturn <- function(p) {
  r <- rep(NA, length(p)-1)
  for (i in 1 : length(r)) {
    r[i] = abs(log(p[i+1] / p[i]))
  }
  return (r)
}

spread <- function(p) {
  s <- rep(NA, length(p)-1)
  for (i in 1 : length(s)) {
    s[i] = abs(p[i+1] - p[i])
  }
  return (s)
}

# bulk process
r_ac  <- rep(NA, SIMULATION_RUN)    # auto-correlation return
r_abs_ac <- rep(NA, SIMULATION_RUN) # auto-correlation absolute return
r_kt <- rep(NA, SIMULATION_RUN)     # kurtosis return
p_sd <- rep(NA, SIMULATION_RUN)     # p_t sd, volatility
p_f_sd <- rep(NA, SIMULATION_RUN)     # p_t sd, volatility
s_mu <- rep(NA, SIMULATION_RUN)     # spread mu
c_n  <- rep(NA, SIMULATION_RUN)     # crash number
c_max <- rep(NA, SIMULATION_RUN)    # max crash, meause in change price proportion
c_min <- rep(NA, SIMULATION_RUN)    # min crash, meause in change price proportion
w_mu <- rep(NA, SIMULATION_RUN)     # wealth mu, calculated using last 1000 steps 
w_sd <- rep(NA, SIMULATION_RUN)     # wealth sd, calculated using last 1000 steps
f_mu <- rep(NA, SIMULATION_RUN)     # freq mu
f_sd <- rep(NA, SIMULATION_RUN)     # freq sd


for (i in 1 : SIMULATION_RUN) {
  # read data
  mkt <- read.table(paste(ROOT, FOLDER, FILE, i-1, sep=""), header=TRUE, sep=",")
  mkt <- mkt[c(0:-TRAINING_T),]
  
  cat("data:", i, "\n")
  r_ac[i] <- acf(logReturn(mkt$p_lt), plot=FALSE)$acf[2]
  r_abs_ac[i] <- acf(absLogReturn(mkt$p_lt), plot=FALSE)$acf[2]
  r_kt[i] <- kurtosis(logReturn(mkt$p_lt))
  
  p_sd[i] <- sd(mkt$p_lt)
  p_f_sd[i] <- sd(mkt$p_f_t)
  s_mu[i] <- mean(spread(mkt$p_lt))
  #pc_n[i]  <- countCrash(mkt$lt, mkt$p_lt)
  #margin <- crashMargin(mkt$lt, mkt$p_lt)
  #c_max[i] <- margin[1]
  #c_min[i] <- margin[2]
  
  mkt2 <- mkt[c((nrow(mkt)-TRAINING_T) : nrow(mkt)),]
  w_mu[i] <- mean(mkt2$hft_wealth_mu)
  w_sd[i] <- mean(mkt2$hft_wealth_sd)
  f_mu[i] <- mean(mkt2$hft_freq_mu)
  f_sd[i] <- mean(mkt2$hft_freq_sd)
  
  a <- spread(mkt$p_lt)
}

# report
rst <- data.frame(r_ac, r_abs_ac, r_kt, 
                  p_sd, s_mu, c_n, c_max, c_min,
                  w_mu, w_sd, f_mu, f_sd)
rpt <- data.frame(mean(r_ac), mean(r_abs_ac), mean(r_kt),
  mean(p_sd), mean(s_mu), mean(c_n), mean(c_max), mean(c_min),
  mean(w_mu), mean(w_sd), mean(f_mu), mean(f_sd))