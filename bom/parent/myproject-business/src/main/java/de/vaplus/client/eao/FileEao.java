package de.vaplus.client.eao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.vaplus.api.entity.ActivityOwner;
import de.vaplus.api.entity.Base;
import de.vaplus.api.entity.Customer;
import de.vaplus.api.entity.File;
import de.vaplus.api.entity.FileActivity;
import de.vaplus.api.entity.Shop;
import de.vaplus.api.entity.User;
import de.vaplus.client.entity.DBFileEntity;
import de.vaplus.client.entity.FileActivityEntity;
import de.vaplus.client.entity.FileActivityEntity_;
import de.vaplus.client.entity.FileEntity;
import de.vaplus.client.entity.FileEntity_;
import de.vaplus.client.entity.FileSystemFileEntity;
import de.vaplus.client.entity.FileSystemFileEntity_;


@Stateless
public class FileEao implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3466161576414849348L;

	@PersistenceContext(unitName="vaplus-client")
    private EntityManager em;

    public File saveFile(FileEntity file){
    	return em.merge(file);
    }

	public File getFile(String code) {
		File file = null;

		try{

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<FileEntity> cQuery = cb.createQuery(FileEntity.class);
			Root<FileEntity> root = cQuery.from(FileEntity.class);
			cQuery.select(root);

			cQuery.where(
			        cb.equal(
			                root.get(FileEntity_.code),code
			        )
				);

			file = em.createQuery(cQuery).setMaxResults(1).getSingleResult();
		}
		catch(NoResultException e){
		}

		return file;
	}

	public List<? extends FileActivity> getOwnerFileList(ActivityOwner owner, Base fileRelation, boolean onlyVisible) {
		List<? extends FileActivity> list = new LinkedList<FileActivity>();

		try{

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<FileActivityEntity> cQuery = cb.createQuery(FileActivityEntity.class);
			Root<FileActivityEntity> root = cQuery.from(FileActivityEntity.class);
			cQuery.select(root);

			List<Predicate> criteria = new ArrayList<Predicate>();
			

			if(owner instanceof User){
				criteria.add( cb.equal( root.get(FileActivityEntity_.user),owner ) );
			}else if(owner instanceof Customer){
				criteria.add( cb.equal( root.get(FileActivityEntity_.customer),owner ) );
			}else if(owner instanceof Shop){
				criteria.add( cb.equal( root.get(FileActivityEntity_.shop),owner ) );
			}else if(owner == null){
				criteria.add( cb.isNull( root.get(FileActivityEntity_.user) ) );
				criteria.add( cb.isNull( root.get(FileActivityEntity_.customer) ) );
				criteria.add( cb.isNull( root.get(FileActivityEntity_.shop) ) );
			}else{
				return list;
			}
				
			
			if(onlyVisible == true){
				criteria.add( cb.equal( root.get(FileActivityEntity_.invisible), false ) );
			}

			if(fileRelation != null){

				criteria.add( cb.equal( root.get(FileActivityEntity_.relationClass),fileRelation.getClass().getSimpleName() ) );
				criteria.add( cb.equal( root.get(FileActivityEntity_.relation),fileRelation.getId() ) );
				
			}

			if(criteria.size() > 0)
				cQuery.where(cb.and(criteria.toArray(new Predicate[0])));

			cQuery.orderBy(cb.desc(root.get(FileActivityEntity_.updateDate)));

			list = em.createQuery(cQuery).getResultList();


		}
		catch(NoResultException e){
		}

		return list;
	}



	public long getFileConsumption() {

		try{

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> cQuery = cb.createQuery(Long.class);
			Root<FileSystemFileEntity> root = cQuery.from(FileSystemFileEntity.class);

			// build SUM(A * (100.0 - B) / 100.0) expression
			Expression<Long> sum = cb.sum(root.get(FileSystemFileEntity_.size));
			cQuery.select(sum);

			return em.createQuery(cQuery).getSingleResult();

		}
		catch(NoResultException e){
		}
		catch(NullPointerException e){
		}

		return 0;
	}

	public void deleteDBFile(DBFileEntity file) {
		if(file == null)
			return;

		DBFileEntity f = em.find(DBFileEntity.class, file.getId());
		em.remove(f);
	}

	public void deleteFileSystemFile(FileSystemFileEntity file) {
		if(file == null)
			return;

		FileSystemFileEntity f = em.find(FileSystemFileEntity.class, file.getId());
		em.remove(f);
	}

	public void deleteFileActivityEntity(FileActivityEntity file) {
		if(file == null)
			return;

		FileActivityEntity f = em.find(FileActivityEntity.class, file.getId());
		em.remove(f);
	}

	public DBFileEntity getDBFile(long id) {
		return em.find(DBFileEntity.class, id);
	}

	public void flush() {
		em.flush();
	}

}
