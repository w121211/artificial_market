source("~/git/artificial_market/r/artificial_market/global.R")
library("PerformanceAnalytics")
library("R.utils")
#MM_FOLDER = "EXP-01/rf-pm-nn-10-%.1f-0.00/"
FOLDER = "CLB-03.1/rf-pm-nn-00-0.9-0.1x2.17/"
#FOLDER = "EXP-01/rf-nn-nn-00-0.0-0.00/"
SIMULATION_RUN = 1
#lambda_set <- seq(0.1, 1, 0.1)
lambda_set <- c(0.0)

# bulk process
d <- dataFrame(nrow=SIMULATION_RUN, colClasses=c(
  p_mu="double",
  p_sd="double",
  p_min="double",
  p_max="double",
  p_max_min="double",
  spread="double",
  r_mu="double",
  r_me="double",
  r_sd="double",
  r_kt="double",
  r_sk="double",
  r_acf="double",
  abs_r_acf="double",
  trd_sum="double",
  trd_mu="double",
  trd_ll_sum="double",
  trd_lh_sum="double",
  trd_hh_sum="double",
  depth_buy_mu="double",
  depth_buy_min="double",
  depth_buy_q1="double",
  depth_buy_q2="double",
  depth_buy_q3="double",
  depth_buy_max="double",
  depth_sell_mu="double",
  depth_sell_min="double",
  depth_sell_q1="double",
  depth_sell_q2="double",
  depth_sell_q3="double",
  depth_sell_max="double",
  pos_r_max="double",
  neg_r_max="double",
  hft_asset_mu="double",
  hft_asset_sd="double",
  hft_freq_mu="double",
  hft_s_mean="double",
  hft_s_sd="double",
  hft_s_min="double",
  hft_s_max="double",
  hft_pos_s_mu="double",
  hft_neg_s_mu="double",
  lft_bodr_vol_sum="double",
  lft_sodr_vol_sum="double",
  lft_p_mean="double",
  lft_p_stdev="double",
  lft_p_min="double",
  lft_p_max="double",
  lft_p_max_min="double"
  ))
atom2 <- data.frame()

for (j in 1 : length(lambda_set)) {
  if (!exists("FOLDER")) {
    FOLDER = sprintf(MM_FOLDER, lambda_set[j])
  }
for (i in 1 : SIMULATION_RUN) {
  cat(sprintf("%d.%d ", j, i))
  
  # p_lt
  FILE = 'marketlt'
  mkt <- read.table(paste(ROOT, FOLDER, FILE, i-1, sep=""), header=TRUE, sep=",")
  mkt <- subset(mkt, lt > TRAINING_T)
  #mkt <- mkt[complete.cases(mkt), ]  # MM
  d$spread[i] <- mean(mkt$spread)    # MM
  d$spread[i] <- mean(mkt$spread[complete.cases(mkt$spread)]) # for No MM
  d$p_mu[i] <- mean(mkt$p_lt)
  d$p_sd[i] <- sd(mkt$p_lt)
  d$p_min[i] <- min(mkt$p_lt)
  d$p_max[i] <- max(mkt$p_lt)
  d$p_max_min[i] <- max(mkt$p_lt) - min(mkt$p_lt)
  
  # log return
  r <- logReturn(mkt$p_lt)
  d$r_mu[i] <- mean(r)
  d$r_me[i] <- median(r)
  d$r_sd[i] <- sd(r)
  d$r_kt[i] <- kurtosis(r)
  d$r_sk[i] <- skewness(r)
  d$r_acf[i] <- acf(r, plot=FALSE)$acf[2]
  d$abs_r_acf[i] <- acf(abs(r), plot=FALSE)$acf[2]
  
  # trade
  FILE = "trade"
  trd <- read.table(paste(ROOT, FOLDER, FILE, i-1, sep=""), header=TRUE, sep=",")
  trd <- subset(trd, t > mkt$t[lt=TRAINING_T])
  trd_ll <- subset(trd, demandAgentType == "L" & supplyAgentType == "L")
  trd_lh <- subset(trd, (demandAgentType == "L" & supplyAgentType == "H") | 
    (demandAgentType == "H" & supplyAgentType == "L"))
  trd_hh <- subset(trd, demandAgentType == "H" & supplyAgentType == "H")
  
  d$trd_sum[i] <- sum(trd$volume)
  d$trd_mu[i] <- mean(trd$volume)
  d$trd_ll_sum[i] <- sum(trd_ll$volume)
  d$trd_lh_sum[i] <- sum(trd_lh$volume)
  d$trd_hh_sum[i] <- sum(trd_hh$volume)
  
  # market depth
  FILE = "orderbook"
  odb <- read.table(paste(ROOT, FOLDER, FILE, i-1, sep=""), header=TRUE, sep=",")
  odb <- subset(odb, lt > TRAINING_T)
  s <- summary(odb$hba + odb$lba) # buy depth
  d$depth_buy_mu[i] <- s[4]
  d$depth_buy_min[i] <- s[1]
  d$depth_buy_q1[i] <- s[2]
  d$depth_buy_q2[i] <- s[3]
  d$depth_buy_q3[i] <- s[5]
  d$depth_buy_max[i] <- s[6]
  s <- summary(odb$hsa + odb$lsa) # sell depth
  d$depth_sell_mu[i] <- s[4]
  d$depth_sell_min[i] <- s[1]
  d$depth_sell_q1[i] <- s[2]
  d$depth_sell_q2[i] <- s[3]
  d$depth_sell_q3[i] <- s[5]
  d$depth_sell_max[i] <- s[6]
  
  # crashes/spikes
  pos_r <- r[r>0]
  neg_r <- r[r<0]
  d$pos_r_max[i] <- max(pos_r)
  d$neg_r_max[i] <- max(abs(neg_r))
  
  # HFT
  PERIOD = 100
  d$hft_asset_mu[i] <- mean(mkt[(nrow(mkt)-PERIOD):nrow(mkt),'hft_wealth_mu'])
  d$hft_asset_sd[i] <- mean(mkt[(nrow(mkt)-PERIOD):nrow(mkt),'hft_wealth_sd'])
  d$hft_freq_mu[i] <- mean(mkt[(nrow(mkt)-PERIOD):nrow(mkt),'hft_freq_mu'])
  FILE = "agent"
  agt <- read.table(paste(ROOT, FOLDER, FILE, i-1, sep=""), header=TRUE, sep=",")
  agt_hft <- subset(agt, t >= mkt$t[1] & grepl("H", id))
  s <- summary(agt_hft$stock)
  d$hft_s_mean[i] <- s[4]
  d$hft_s_sd[i] <- sd(agt_hft$stock)
  d$hft_s_min[i] <- s[1]
  d$hft_s_max[i] <- s[6]
  d$hft_pos_s_mu[i] <- mean(agt_hft$stock[agt_hft$stock > 0])
  d$hft_neg_s_mu[i] <- mean(agt_hft$stock[agt_hft$stock < 0])
  
  # LFT
  FILE = "order"
  odr <- read.table(paste(ROOT, FOLDER, FILE, i-1, sep=""), header=TRUE, sep=",")
  odr <- subset(odr, lt > TRAINING_T)
  lft_odr <- subset(odr, agentType == 'L')
  lft_bodr <- subset(odr, agentType == 'L' & buysell == 'BUY')
  lft_sodr <- subset(odr, agentType == 'L' & buysell == 'SELL')
  d$lft_bodr_vol_sum[i] <- sum(lft_bodr$volume)
  d$lft_sodr_vol_sum[i] <- sum(lft_sodr$volume)
  d$lft_p_mean[i] <- mean(lft_odr$price)
  d$lft_p_stdev[i] <- sd(lft_odr$price)
  d$lft_p_min[i] <- min(lft_odr$price)
  d$lft_p_max[i] <- max(lft_odr$price)
  d$lft_p_max_min[i] <- max(lft_odr$price) - min(lft_odr$price)
}
  # report
  d2 <- d[complete.cases(d), ]
  atom <- lapply(d2, median)
  #atom$pos_r_max[1] <- max(d$pos_r_max)
  #atom$neg_r_max[1] <- max(d$neg_r_max)
  atom2 <- rbind(atom2, atom)
}

write.table(atom2, file="artificial_market/zout", sep=",", row.names=F, col.names=T)
