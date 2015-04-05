package net.ezshop.service.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.ezshop.dao.PaymentDao;
import net.ezshop.entity.Member;
import net.ezshop.entity.Order;
import net.ezshop.entity.Payment;
import net.ezshop.entity.Payment.Status;
import net.ezshop.entity.Payment.Type;
import net.ezshop.service.MemberService;
import net.ezshop.service.OrderService;
import net.ezshop.service.PaymentService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 收款单
 */
@Service("paymentServiceImpl")
public class PaymentServiceImpl extends BaseServiceImpl<Payment, Long> implements PaymentService {

	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;
	@Resource(name = "orderServiceImpl")
	private OrderService orderService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "paymentDaoImpl")
	public void setBaseDao(PaymentDao paymentDao) {
		super.setBaseDao(paymentDao);
	}

	@Transactional(readOnly = true)
	public Payment findBySn(String sn) {
		return paymentDao.findBySn(sn);
	}

	public void handle(Payment payment) {
		paymentDao.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
		if (payment != null && payment.getStatus() == Status.wait) {
			if (payment.getType() == Type.payment) {
				Order order = payment.getOrder();
				if (order != null) {
					orderService.payment(order, payment, null);
				}
			} else if (payment.getType() == Type.recharge) {
				Member member = payment.getMember();
				if (member != null) {
					memberService.update(member, null, payment.getEffectiveAmount(), null, null);
				}
			}
			payment.setStatus(Status.success);
			payment.setPaymentDate(new Date());
			paymentDao.merge(payment);
		}
	}

}