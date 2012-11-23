source("~/git/artificial_market/r/artificial_market/global.R")
library("PerformanceAnalytics")
FOLDER = "SF-01.1/sf-sm-nn-10-0.5-0.1/"
SIMULATION_RUN = 100
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
p_mu <- rep(NA, SIMULATION_RUN)      # p_t mean
p_sd <- rep(NA, SIMULATION_RUN)      # p_t std dev, volatility
p_min <- rep(NA, SIMULATION_RUN)     # p_t min
p_max <- rep(NA, SIMULATION_RUN)     # p_t max
p_max_min <- rep(NA, SIMULATION_RUN) # p_t (max-min)

p_f_sd <- rep(NA, SIMULATION_RUN)   # p_f_t sd, volatility

r_mu  <- rep(NA, SIMULATION_RUN)    # log return mean
r_sd  <- rep(NA, SIMULATION_RUN)    # log return std dev
r_min  <- rep(NA, SIMULATION_RUN)    # log return min
r_max  <- rep(NA, SIMULATION_RUN)    # log return max
r_kt <- rep(NA, SIMULATION_RUN)     # kurtosis of log return
r_ac  <- rep(NA, SIMULATION_RUN)    # auto-correlation of log return
abs_r_ac <- rep(NA, SIMULATION_RUN) # auto-correlation of absolute log return

ol_bvol_mean <- rep(NA, SIMULATION_RUN) # LFT order buy volume mean
ol_svol_mean <- rep(NA, SIMULATION_RUN) # LFT order sell volume mean
o_hb_vol_mean <- rep(NA, SIMULATION_RUN) # HFT order buy volume mean
o_hs_vol_mean <- rep(NA, SIMULATION_RUN) # HFT order sell volume mean

#s_mu <- rep(NA, SIMULATION_RUN)     # spread mu
#c_n  <- rep(NA, SIMULATION_RUN)     # crash number
#c_max <- rep(NA, SIMULATION_RUN)    # max crash, meause in change price proportion
#c_min <- rep(NA, SIMULATION_RUN)    # min crash, meause in change price proportion
#w_mu <- rep(NA, SIMULATION_RUN)     # wealth mu, calculated using last 1000 steps 
#w_sd <- rep(NA, SIMULATION_RUN)     # wealth sd, calculated using last 1000 steps
#f_mu <- rep(NA, SIMULATION_RUN)     # freq mu
#f_sd <- rep(NA, SIMULATION_RUN)     # freq sd

for (i in 1 : SIMULATION_RUN) {
  # read data
  FILE = 'marketlt'
  mkt <- read.table(paste(ROOT, FOLDER, FILE, i-1, sep=""), header=TRUE, sep=",")
  mkt <- mkt[c(0:-TRAINING_T),]
  cat("data:", i, "\n")
  
  # p_lt
  p_mu[i] <- mean(mkt$p_lt)
  p_sd[i] <- sd(mkt$p_lt)
  p_min[i] <- min(mkt$p_lt)
  p_max[i] <- max(mkt$p_lt)
  p_max_min[i] <- max(mkt$p_lt) - min(mkt$p_lt)
  #s_mu[i] <- mean(spread(mkt$p_lt))
  #p_f_sd[i] <- sd(mkt$p_f_t)
  
  # log return
  r <- logReturn(mkt$p_lt)
  r_mu[i] <- mean(r)
  r_sd[i] <- sd(r)
  r_min[i] <- min(r)
  r_max[i] <- max(r)
  r_kt[i] <- kurtosis(r)
  r_ac[i] <- acf(r, plot=FALSE)$acf[2]
  
  # absolute log return
  abs_r_ac[i] <- acf(absLogReturn(mkt$p_lt), plot=FALSE)$acf[2]
  
  # order volume
  FILE = 'order'
  odr <- read.table(paste(ROOT, FOLDER, FILE, i-1, sep=""), header=TRUE, sep=",")
  buy_odr <- subset(odr, !(lt < TRAINING_T) & buysell=="BUY")
  sell_odr <- subset(odr, !(lt < TRAINING_T) & buysell=="SELL")
  ol_bvol_mean[i] <- mean(buy_odr$volume)
  ol_svol_mean[i] <- mean(sell_odr$volume)
  
  #mkt2 <- mkt[c((nrow(mkt)-TRAINING_T) : nrow(mkt)),]
  #w_mu[i] <- mean(mkt2$hft_wealth_mu)
  #w_sd[i] <- mean(mkt2$hft_wealth_sd)
  #f_mu[i] <- mean(mkt2$hft_freq_mu)
  #f_sd[i] <- mean(mkt2$hft_freq_sd)
}

# report
rpt <- data.frame(
  mean(p_mu), mean(p_sd), mean(p_min), mean(p_max), mean(p_max_min),
  mean(r_mu), mean(r_sd), mean(r_min), mean(r_max), mean(r_kt), mean(r_ac), mean(abs_r_ac),
  mean(ol_bvol_mean), mean(ol_svol_mean))