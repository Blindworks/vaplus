package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.AccountingAssignment;
import de.vaplus.api.entity.CommissionTypeAssociation;
import de.vaplus.api.entity.ManualCommission;
import de.vaplus.api.entity.Vendor;
import de.vaplus.client.entity.ManualCommissionEntity;
import de.vaplus.client.entity.commission.vendor.AccountingAssignmentEntity;
import de.vaplus.client.entity.commission.vendor.AccountingAssignmentEntity_;
import de.vaplus.client.entity.commission.vendor.CommissionTypeAssociationEntity;
import de.vaplus.client.entity.commission.vendor.VendorCommissionAccountingFileEntity;
import de.vaplus.client.entity.commission.vendor.VendorCommissionAccountingFileEntity_;


@Stateless
public class AccountingEao implements Serializable {

	private static final long serialVersionUID = -7905930464258403023L;
	
	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

	public void flush(){
		em.flush();
	}

	public VendorCommissionAccountingFileEntity saveVendorCommissionAccountingFile(
			VendorCommissionAccountingFileEntity commissionAccountingFile) {
		return em.merge(commissionAccountingFile);
	}

	public VendorCommissionAccountingFileEntity getCommissionAccountingFile(String vo, int year, int month){
		try{
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<VendorCommissionAccountingFileEntity> cQuery = cb.createQuery(VendorCommissionAccountingFileEntity.class);
			Root<VendorCommissionAccountingFileEntity> root = cQuery.from(VendorCommissionAccountingFileEntity.class);
			cQuery.select(root);

			cQuery.where(
					cb.and(
						cb.equal(root.get(VendorCommissionAccountingFileEntity_.vo), vo),
						cb.equal(root.get(VendorCommissionAccountingFileEntity_.year), year),
						cb.equal(root.get(VendorCommissionAccountingFileEntity_.month), month)
					)
						
			);

			return em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}
		
		return null;
	}

	public List<VendorCommissionAccountingFileEntity> getCommissionAccountingFileList(int year, int month){
		try{
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<VendorCommissionAccountingFileEntity> cQuery = cb.createQuery(VendorCommissionAccountingFileEntity.class);
			Root<VendorCommissionAccountingFileEntity> root = cQuery.from(VendorCommissionAccountingFileEntity.class);
			cQuery.select(root);

			cQuery.where(
					cb.and(
						cb.equal(root.get(VendorCommissionAccountingFileEntity_.year), year),
						cb.equal(root.get(VendorCommissionAccountingFileEntity_.month), month)
					)
						
			);

			return em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return null;
	}

	public CommissionTypeAssociation getCommissionTypeAssociationByText(
			String commissiontText, String commissiontSubText) {
		
		CommissionTypeAssociation ate = null;
			try{
				ate = em.createNamedQuery("CommissionTypeAssociation.findByText",CommissionTypeAssociationEntity.class)
			            .setParameter("commissionText", commissiontText)
			            .setParameter("commissionSubText", commissiontSubText)
			            .setMaxResults(1)
			            .getSingleResult();
			}
			catch(NoResultException e){
			}
			
		return ate;
	}

	public CommissionTypeAssociation saveUnknownCommissionTypeAssociation(
			CommissionTypeAssociation commissionTypeAssociation) {
		return em.merge(commissionTypeAssociation);
	}

	public List<? extends AccountingAssignment> getAccountingAssignmentList( Vendor vendor, int commissionYear, int commissionMonth) {
		try{
			
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<AccountingAssignmentEntity> cQuery = cb.createQuery(AccountingAssignmentEntity.class);
			Root<AccountingAssignmentEntity> root = cQuery.from(AccountingAssignmentEntity.class);
			cQuery.select(root);

			cQuery.where(
					cb.and(
						cb.equal(root.get(AccountingAssignmentEntity_.vendor), vendor),
						cb.equal(root.get(AccountingAssignmentEntity_.commissionYear), commissionYear),
						cb.equal(root.get(AccountingAssignmentEntity_.commissionMonth), commissionMonth)
					)
						
			);
			
			cQuery.orderBy(cb.asc(root.get(AccountingAssignmentEntity_.date)));

			return em.createQuery(cQuery).getResultList();
		}
		catch(NoResultException e){
		}
		
		return null;
	}

	public AccountingAssignmentEntity saveAccountingAssignment(AccountingAssignmentEntity aa){
		aa = em.merge(aa);
		
		return aa;
	}

	public void remove(AccountingAssignment aa) {
		
		AccountingAssignmentEntity aae = em.find(AccountingAssignmentEntity.class, aa.getId());
		em.remove(aae);
	}

	public List<? extends CommissionTypeAssociation> getCommissionTypeAssociationList(
			Vendor vendor) {
		
		List<? extends CommissionTypeAssociation> list = new LinkedList<CommissionTypeAssociation>();
		
			try{
				list = em.createNamedQuery("CommissionTypeAssociation.getList",CommissionTypeAssociationEntity.class)
			            .setParameter("vendor", vendor)
			            .getResultList();
			}
			catch(NoResultException e){
			}
			
		return list;
	}

	public void deleteCommissionTypeAssociation(
			CommissionTypeAssociation commissionTypeAssociation) {

		CommissionTypeAssociationEntity cta = em.find(CommissionTypeAssociationEntity.class, commissionTypeAssociation.getId());
		em.remove(cta);
	}

	public ManualCommission saveManualCommission(ManualCommission manualCommission) {
		return em.merge(manualCommission);
	}

	public List<? extends ManualCommission> getManualCommissionList() {
		
		List<? extends ManualCommission> list = new LinkedList<ManualCommission>();
		
			try{
				
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<ManualCommissionEntity> cQuery = cb.createQuery(ManualCommissionEntity.class);
				Root<ManualCommissionEntity> root = cQuery.from(ManualCommissionEntity.class);
				cQuery.select(root);
				
				return em.createQuery(cQuery).getResultList();
			}
			catch(NoResultException e){
			}
			
		return list;
	}

	public void refresh(AccountingAssignment aa) {
		em.refresh(aa);
	}
}
